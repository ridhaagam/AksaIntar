package com.capstone.aksaintar.ui.views.contributor

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.viewmodel.compose.viewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

private const val CAMERA_PERMISSION_CODE = 123

@Composable
fun UploadScreen(

    viewModel: UploadViewModel = viewModel(),

    ) {


    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var category by remember { mutableStateOf("") }

    val context = LocalContext.current


    val launcherGallery = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            photoUri = it
            bitmap = it?.let { it1 -> loadBitmapFromUri(context, it1) }
        }
    )
    val launcherCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isTaken ->
            if (isTaken) {
                photoUri?.let {
                    bitmap = loadBitmapFromUri(context, it)
                }
            }
        }
    )

    val cameraPermission = arrayOf(Manifest.permission.CAMERA)

    // Display image if there is one
    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = "Image from the gallery or camera",
            Modifier.size(400.dp)
        )

        Spacer(modifier = Modifier.padding(20.dp))

    }

    Spacer(modifier = Modifier.padding(20.dp))

    TextField(
        value = category,
        onValueChange = { category = it },
        label = { Text("Category") }
    )

    Row {
        Button(onClick = { launcherGallery.launch("image/*") }) {
            Text("Pick from gallery")
        }
        Button(onClick = {
            if (EasyPermissions.hasPermissions(context, *cameraPermission)) {
                val values = ContentValues()
                val resolver = context.contentResolver
                val uri =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                uri?.let {
                    photoUri = it
                    launcherCamera.launch(it)
                }
            } else {
                val rationale = "Camera permission is required to take pictures"
                EasyPermissions.requestPermissions(
                    PermissionRequest.Builder(
                        context as ComponentActivity,
                        CAMERA_PERMISSION_CODE,
                        *cameraPermission
                    )
                        .setRationale(rationale)
                        .build()
                )
            }
        }) {
            Text(text = "Take a picture")
        }
        Button(onClick = {
            val categoryBody = category.toRequestBody("text/plain".toMediaTypeOrNull())
            val imageBody = photoUri?.let { uri ->
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val adjustedBitmap = adjustImageOrientation(context, uri)
                val file = createTempFileWithBitmap(adjustedBitmap, "upload", ".jpeg", context.cacheDir)
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("file", file.name, requestFile)
            }

            if (imageBody != null) {
                viewModel.uploadImage(categoryBody, imageBody)
            }
        }) {
            Text("Upload")
        }
    }


}

private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    } else {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        val bitmap = ImageDecoder.decodeBitmap(source)
        bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }
}

private fun adjustImageOrientation(context: Context, imageUri: Uri): Bitmap {
    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
    val inputStream = context.contentResolver.openInputStream(imageUri)
    val exif = inputStream?.let { ExifInterface(it) }
    val orientation = exif?.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )
    val matrix = Matrix()
    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
    }
    return Bitmap.createBitmap(
        bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
    )
}
private fun createTempFileWithBitmap(bitmap: Bitmap, name: String, extension: String, directory: File): File {
    val file = File.createTempFile(name, extension, directory)
    val bos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
    val bitmapdata = bos.toByteArray()

    FileOutputStream(file).use { fos ->
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
    }
    return file
}



@Composable
@Preview
fun UploadScreenPreview() {
    MaterialTheme {
        UploadScreen(
            viewModel = UploadViewModel(

            )
        )
    }
}
