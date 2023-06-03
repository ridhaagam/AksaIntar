package com.capstone.aksaintar.ui.screen.camera

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.capstone.aksaintar.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    viewModel: CameraViewModel = hiltViewModel(),
) {

    val permission = if (Build.VERSION.SDK_INT <= 28) {
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    } else listOf(
        Manifest.permission.CAMERA
    )

    val permissionState = rememberMultiplePermissionsState(
        permissions = permission
    )

    if (!permissionState.allPermissionsGranted) {
        SideEffect {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    var previewView: PreviewView

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxSize()
    ) {
        // Show camera preview once all permissions are granted
        if (permissionState.allPermissionsGranted) {
            AndroidView(
                factory = {
                    previewView = PreviewView(it)
                    viewModel.showCameraPreview(previewView, lifecycleOwner)
                    previewView
                },
                modifier = Modifier
                    .fillMaxSize()

            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight * 0.9f)

        ,

        contentAlignment = Alignment.BottomCenter // Mengatur contentAlignment menjadi BottomCenter
    ) {
        Row(
            modifier = Modifier
                .width(screenWidth),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    // TODO: Implement pick image from gallery functionality
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_photo_library_24),
                    contentDescription = "Pick from Gallery",
                    modifier = Modifier.size(40.dp),
                    tint = Color.White,
                )
            }
            IconButton(
                onClick = {
                    if (permissionState.allPermissionsGranted) {
                        viewModel.captureAndSaveImage(context)
                    } else {
                        Toast.makeText(context, "Please grant all permissions", Toast.LENGTH_LONG).show()
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_circle_24),
                    contentDescription = "Capture",
                    modifier = Modifier.size(70.dp),
                    tint = Color.White,
                )
            }

            IconButton(
                onClick = {
                    // TODO: Implement flip camera functionality
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_flip_camera_android_24),
                    contentDescription = "Flip Camera",
                    modifier = Modifier.size(40.dp),
                    tint = Color.White,
                )
            }
        }
    }
}



