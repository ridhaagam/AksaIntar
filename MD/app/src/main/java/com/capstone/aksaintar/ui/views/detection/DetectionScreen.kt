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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Halaman Deteksi Objek", style =MaterialTheme.typography.body1 ) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Kembali ke halaman utama"
                        )
                    }
                }
            )
        }, content = {
            Column(
                Modifier
                    .fillMaxSize()
                    .semantics {
//                        contentDescription = "Halaman Deteksi Objek"
                    },

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Gambar Pratinjau ",
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
                            Text(text = "Gambar yang terdeteksi adalah:",style =MaterialTheme.typography.body1, color = warna )
                            Text(text = classification, color = warna, fontSize = 24.sp, style =MaterialTheme.typography.body1 )
//                            Toast.makeText(context, "Gambar yang terdeteksi  $classification", Toast.LENGTH_SHORT).show()
                            if (classification == "Tidak Diketahui") {
                                Toast.makeText(context, "Gambar yang terdeteksi $classification, coba lagi", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Gambar yang terdeteksi adalah $classification", Toast.LENGTH_SHORT).show()
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
                    OutlinedButton(onClick = {
                        launcherGallery.launch("image/*")
                    },colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.Transparent,
                    ),
                        border = BorderStroke(2.dp, colors.primary),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20),) {
                        Text(text = "Ambil Gambar dari Galeri", style =MaterialTheme.typography.body1, color = warna )
                    }

                    OutlinedButton(onClick = {
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
                            val rationale = "Izin kamera diperlukan untuk mengambil gambar"
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
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = Color.Transparent,
                        ),
                        border = BorderStroke(2.dp, colors.primary),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20),) {
                        Text(
                            text = "Ambil Gambar dengan Kamera",
                            style =MaterialTheme.typography.body1,
                            color = warna
                        )

                    }
                }
            }
        })
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

