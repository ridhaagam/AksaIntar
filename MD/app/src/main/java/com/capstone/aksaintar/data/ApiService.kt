package com.capstone.aksaintar.data


import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("upload")
    suspend fun uploadImage(@Part image: MultipartBody.Part, @Part("category") category: String): Call<UploadResponse>
}


