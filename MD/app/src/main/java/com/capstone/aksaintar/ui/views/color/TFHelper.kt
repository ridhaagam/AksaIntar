package com.capstone.aksaintar.ui.views.color


import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.capstone.aksaintar.ml.Colormodel
import com.capstone.aksaintar.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

object TFHelper {

    val imageSize = 64
    val numChannels = 3

    @Composable
    fun classifyColor(image: Bitmap, callback: (@Composable (fruit: String) -> Unit)) {
        val model: Colormodel = Colormodel.newInstance(LocalContext.current)

        // Resize the image to match the input size
        val resizedImage = Bitmap.createScaledBitmap(image, imageSize, imageSize, true)

        // Creates inputs for reference.
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, imageSize, imageSize, numChannels), DataType.FLOAT32)
        val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * numChannels)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(imageSize * imageSize)
        resizedImage.getPixels(intValues, 0, resizedImage.width, 0, 0, resizedImage.width, resizedImage.height)
        var pixel = 0
        // Iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
        for (i in 0 until imageSize) {
            for (j in 0 until imageSize) {
                val value = intValues[pixel++] // RGB
                byteBuffer.putFloat((value shr 16 and 0xFF) * (1f / 255))
                byteBuffer.putFloat((value shr 8 and 0xFF) * (1f / 255))
                byteBuffer.putFloat((value and 0xFF) * (1f / 255))
            }
        }


        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs: Colormodel.Outputs = model.process(inputFeature0)
        val outputFeature0: TensorBuffer = outputs.outputFeature0AsTensorBuffer
        val confidences = outputFeature0.floatArray
        // Find the index of the class with the highest confidence.
        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        val classes = arrayOf(
            "hitam",
            "biru",
            "cokelat",
            "hijau",
            "merah muda",
            "merah",
            "perak",
            "putih",
            "kuning"
        )

        callback.invoke(classes[maxPos])

        // Releases model resources if no longer used.
        model.close()
    }
}

