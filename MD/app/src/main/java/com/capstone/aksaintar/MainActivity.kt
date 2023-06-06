package com.capstone.aksaintar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.capstone.aksaintar.ui.theme.AksaIntarTheme
import pub.devrel.easypermissions.EasyPermissions


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AksaIntarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AksaIntarApp()
                }
            }
        }
    }

    // onRequestPermissionsResult dipanggil setelah permintaan izin direspon oleh pengguna
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Menggunakan EasyPermissions untuk mengelola respons izin dengan mudah
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}