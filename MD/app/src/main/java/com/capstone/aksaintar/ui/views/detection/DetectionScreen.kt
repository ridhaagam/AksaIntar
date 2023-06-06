package com.capstone.aksaintar.ui.views.detection

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.aksaintar.ui.theme.AksaIntarTheme
import com.capstone.aksaintar.ui.views.detection.TensorFLowHelper.imageSize
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

private const val CAMERA_PERMISSION_CODE = 123

@Composable
fun ImagePicker() {
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    // rememberLauncherForActivityResult menyimpan status komposisi saat terjadi perubahan konfigurasi
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


    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Image from the gallery or camera",
                Modifier.size(400.dp)
            )

            Spacer(modifier = Modifier.padding(20.dp))

            val scaledBitmap = Bitmap.createScaledBitmap(it, imageSize, imageSize, false)
            TensorFLowHelper.classifyImage(scaledBitmap) { classification ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Image is classified as:")
                    Text(text = classification, color = colors.primary, fontSize = 24.sp)
                }
            }
        }

        Spacer(modifier = Modifier.padding(20.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                launcherGallery.launch("image/*")
            }) {
                Text(text = "Pick from Gallery")
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
        }
    }
}


private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return if (Build.VERSION.SDK_INT < 28) {
        // Jika SDK Android < 28 (versi sebelum Android 9.0), gunakan metode lama untuk mendapatkan bitmap dari URI
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    } else {
        // Jika SDK Android >= 28 (versi Android 9.0 atau lebih baru), gunakan ImageDecoder untuk mendecode bitmap dari URI
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        val bitmap = ImageDecoder.decodeBitmap(source)
        bitmap.copy(Bitmap.Config.ARGB_8888, true) // Konversi ke Config#ARGB_8888
    }
}

@Composable
@Preview(showBackground = true)
fun DefaultPreview() {
    AksaIntarTheme {
        ImagePicker()
    }
}