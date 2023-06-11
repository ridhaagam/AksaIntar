package com.capstone.aksaintar.ui.views.contributor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.aksaintar.data.ApiConfig
import com.capstone.aksaintar.data.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadViewModel : ViewModel() {

    private val _uploadImage = MutableLiveData<UploadResponse>()
    val uploadImage: LiveData<UploadResponse> = _uploadImage


    fun uploadImage(category: RequestBody, file: MultipartBody.Part) {
        val client = ApiConfig.getApiService().uploadImage(category, file)
        client.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>, response: Response<UploadResponse>
            ) {
                if (response.isSuccessful) {
                    _uploadImage.value = response.body()

                } else {
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {

            }

        })
    }


}
