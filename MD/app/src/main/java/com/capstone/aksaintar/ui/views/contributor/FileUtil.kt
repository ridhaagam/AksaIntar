package com.capstone.aksaintar.ui.views.contributor

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

object FileUtil {
    fun getPath(context: Context, uri: Uri): String? {
        var path: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            path = cursor.getString(column_index)
            cursor.close()
        }
        return path
    }
}
