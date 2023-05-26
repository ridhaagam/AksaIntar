package com.capstone.aksaintar.presentation

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.aksaintar.domain.repository.CameraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CameraRepository
) : ViewModel() {


    fun showCameraPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) {
        viewModelScope.launch {
            repository.showCameraPreview(previewView, lifecycleOwner)
        }
    }


    fun captureAndSaveImage(context: Context) {
        viewModelScope.launch {
            repository.captureAndSaveImage(context)
        }
    }

}