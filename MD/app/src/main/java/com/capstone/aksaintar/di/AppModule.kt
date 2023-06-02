package com.capstone.aksaintar.di

import android.app.Application
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.FLASH_MODE_ON
import androidx.camera.lifecycle.ProcessCameraProvider
import com.capstone.aksaintar.data.repository.CameraRepositoryImpl
import com.capstone.aksaintar.domain.repository.CameraRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
//    For Camera Selection Front or Back
    fun provideCameraSelector(): CameraSelector {
        return CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
    }

    @Provides
    @Singleton
    fun provideCameraProvider(application: Application)
            : ProcessCameraProvider {
        return ProcessCameraProvider.getInstance(application).get()

    }

    @Provides
    @Singleton
    fun provideCameraPreview(): Preview {
//        For showing camera preview
        return Preview.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideImageCapture(): ImageCapture {
//        for  capturing image you can set flash mode here and aspect ratio
        return ImageCapture.Builder()
            .setFlashMode(FLASH_MODE_ON)
            .setTargetAspectRatio(AspectRatio.RATIO_16_9)
            .build()
    }

    @Provides
    @Singleton
    fun provideImageAnalysis(): ImageAnalysis {
//        for analyzing image before capturing
        return ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
    }

    @Provides
    @Singleton
    fun provideCameraRepository(
        cameraProvider: ProcessCameraProvider,
        cameraSelector: CameraSelector,
        imageCapture: ImageCapture,
        imageAnalysis: ImageAnalysis,
        preview: Preview,


        ): CameraRepository {
        return CameraRepositoryImpl(
            cameraProvider,
            cameraSelector,
            preview,
            imageAnalysis,
            imageCapture
        )
    }

}