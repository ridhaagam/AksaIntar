package com.capstone.aksaintar.ui.views.detection

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.capstone.aksaintar.R
import com.capstone.aksaintar.ui.views.detection.TensorFLowHelper.imageSize
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

private const val CAMERA_PERMISSION_CODE = 123

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ImagePicker(navController: NavController) {
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val warna = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }
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
                title = { Text(stringResource(R.string.title_object_text)) },
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
                Modifier
                    .fillMaxSize()
                    .semantics {
                    },

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = stringResource(R.string.image_preview),
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
                            Text(text = stringResource(R.string.object_result_text), color = warna)
                            Text(text = classification, color = warna, fontSize = 24.sp)
//                            Toast.makeText(context, "Gambar yang terdeteksi  $classification", Toast.LENGTH_SHORT).show()
                            if (classification == "Tidak Diketahui") {
                                Toast.makeText(
                                    context,
                                    "Gambar yang terdeteksi $classification, coba lagi",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Gambar yang terdeteksi adalah $classification",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(5.dp))

                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            launcherGallery.launch("image/*")
                        },
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20),
                    ) {
                        Text(
                            text = stringResource(R.string.gallery_button_text), color = Color.White
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

