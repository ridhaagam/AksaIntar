package com.capstone.aksaintar.ui.views.contributor

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.aksaintar.data.ApiConfig
import com.capstone.aksaintar.data.UploadRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class UploadViewModel(private val activity: Activity) : ViewModel() {
    var imageUri by mutableStateOf<Uri?>(null)

    fun takePicture() {
        if (checkCameraPermission()) {
            startCamera()
        } else {
            requestCameraPermission()
        }
    }

    var imageFile: File? = null

    fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            imageFile = this
            val photoUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(activity, activity.packageName + ".fileprovider", this)
            } else {
                Uri.fromFile(this)
            }
            imageUri = photoUri
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadImage(name: String) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                imageFile?.let { imageFile ->
//                    val requestImageFile = imageFile.asRequestBody("image")
                    val requestImage = MultipartBody.Part.createFormData(
                        "image",
                        imageFile.name,
                        imageFile.asRequestBody("image/*".toMediaType())
                    )
                    val request = UploadRequest(
                        image = requestImage,
                        name = name
                    )
                    ApiConfig.provideApiService().uploadImage(request.image, request.name)
                }
            }
        }
    }



    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CAMERA_PERMISSION
        )
    }
    private fun checkCameraPermission(): Boolean {
        val permission = Manifest.permission.CAMERA
        val granted = PackageManager.PERMISSION_GRANTED
        return ContextCompat.checkSelfPermission(activity, permission) == granted
    }
    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = createImageFile()
        val photoUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(activity, activity.packageName + ".fileprovider", photoFile)
        } else {
            Uri.fromFile(photoFile)
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        activity.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }



    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_CAMERA_PERMISSION = 2
    }
}
