package com.capstone.aksaintar.ui.views.contributor

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.capstone.aksaintar.R
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

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UploadScreen(

    viewModel: UploadViewModel = viewModel(factory = ViewModelFactory(LocalContext.current)),
    navController: NavController

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.contribution_title_text)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            )
        }, content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = stringResource(R.string.image_preview),
                        Modifier.size(400.dp)
                    )


                }

                Spacer(modifier = Modifier.padding(5.dp))

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()

                        .padding(horizontal = 16.dp)
                        .semantics {
                            contentDescription = "Kolom Kategori"
                        },
                    value = category,
                    onValueChange = { category = it },
                    label = { Text(stringResource(R.string.category_label)) }


                )
                Spacer(modifier = Modifier.padding(5.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Button(
                        onClick = { launcherGallery.launch("image/*") },
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20),
                    ) {
                        Text(
                            text = stringResource(R.string.gallery_button_text),
                            color = Color.White

                        )
                    }
                    Button(
                        onClick = {
                            if (EasyPermissions.hasPermissions(context, *cameraPermission)) {
                                val values = ContentValues()
                                val resolver = context.contentResolver
                                val uri =
                                    resolver.insert(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        values
                                    )
                                uri?.let {
                                    photoUri = it
                                    launcherCamera.launch(it)
                                }
                            } else {
                                val rationale = R.string.camera_permission_text
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
                        },
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20),
                    ) {
                        Text(
                            text = stringResource(R.string.camera_button_text),
                            color = Color.White

                        )
                    }


                }
                Button(
                    onClick = {
                        val categoryBody = category.toRequestBody("text/plain".toMediaTypeOrNull())
                        val imageBody = photoUri?.let { uri ->
                            val inputStream = context.contentResolver.openInputStream(uri)
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            val adjustedBitmap = adjustImageOrientation(context, uri)
                            val file = createTempFileWithBitmap(
                                adjustedBitmap,
                                "upload",
                                ".jpeg",
                                context.cacheDir
                            )
                            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                            MultipartBody.Part.createFormData("file", file.name, requestFile)
                        }

                        if (imageBody != null) {
                            viewModel.uploadImage(categoryBody, imageBody)
                        }
                    },
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(20),
                ) {
                    Text(
                        text = stringResource(R.string.upload_text),
                        color = Color.White

                    )
                }


            }
        })
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

private fun createTempFileWithBitmap(
    bitmap: Bitmap,
    name: String,
    extension: String,
    directory: File
): File {
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


