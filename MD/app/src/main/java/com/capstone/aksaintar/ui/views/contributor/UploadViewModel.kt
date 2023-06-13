package com.capstone.aksaintar.ui.views.contributor

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.aksaintar.data.ApiConfig
import com.capstone.aksaintar.data.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadViewModel(private val context: Context) : ViewModel() {

    private val _uploadImage = MutableLiveData<UploadResponse>()


    fun uploadImage(category: RequestBody, file: MultipartBody.Part) {
        val client = ApiConfig.getApiService().uploadImage(category, file)
        client.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>, response: Response<UploadResponse>
            ) {
                if (response.isSuccessful) {
                    _uploadImage.value = response.body()
                    Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}
