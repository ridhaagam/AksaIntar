package com.capstone.aksaintar.ui.views.contributor
import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.aksaintar.ui.views.contributor.UploadViewModel

class UploadViewModelFactory(private val activity: Activity) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UploadViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UploadViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
