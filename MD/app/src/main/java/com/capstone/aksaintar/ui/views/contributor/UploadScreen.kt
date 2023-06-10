package com.capstone.aksaintar.ui.views.contributor

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UploadScreen(viewModel: UploadViewModel = viewModel(
    factory = UploadViewModelFactory(LocalContext.current as Activity)
)) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            val uri = viewModel.imageUri
            val bitmap = uri?.let { loadBitmapFromUri(context, it) }
            imageBitmap = bitmap
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Nama") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val photoFile = viewModel.createImageFile()
            val photoUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(context, context.packageName + ".fileprovider", photoFile)
            } else {
                Uri.fromFile(photoFile)
            }
            takePictureLauncher.launch(photoUri)
        }) {
            Text("Ambil Foto")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.uploadImage(name) }) {
            Text("Upload")
        }
        Spacer(modifier = Modifier.height(16.dp))
        imageBitmap?.let { bitmap ->
            Image(bitmap = bitmap.asImageBitmap(), contentDescription = null)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    } else {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        } ?: throw IllegalArgumentException("Unable to open InputStream for $uri")
    }
}


