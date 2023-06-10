package com.capstone.aksaintar.data

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class UploadRequest(@field:SerializedName("file")val image: MultipartBody.Part, @field:SerializedName("category")val name: String)

