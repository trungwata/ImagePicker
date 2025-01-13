package com.github.dhaval2404.imagepicker.provider

import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.util.lunchPickMultipleImages


class AppImagePicker(
    val activity: AppCompatActivity,
    onResult: (List<Uri>) -> Unit,
    val onCancel: () -> Unit,
) {
    private val pick1Images = registerPickOneImageLauncher(onResult)
    private val pick2Images = registerPickMultipleImageLauncher(onResult, 2)
    private val pick3Images = registerPickMultipleImageLauncher(onResult, 3)
    private val pick4Images = registerPickMultipleImageLauncher(onResult, 4)
    private val pick5Images = registerPickMultipleImageLauncher(onResult, 5)
    private val pickImages = listOf(pick1Images, pick2Images, pick3Images, pick4Images, pick5Images)

    private fun registerPickMultipleImageLauncher(onResult: (List<Uri>) -> Unit, maxImages: Int) =
        activity.registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(maxItems = maxImages)) { imageUris ->
                handlePickedImagesResult(imageUris, onResult)
        }

    private fun registerPickOneImageLauncher(onResult: (List<Uri>) -> Unit) =
        activity.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { imageUri ->
            handlePickedImagesResult(if (imageUri  != null) listOf(imageUri ) else null, onResult)
        }


    fun pickImage(maxNum: Int) {
        assert(maxNum in 1..5) {
            "maxNum must be in range 1 to 5"
        }
        pickImages[maxNum - 1].lunchPickMultipleImages()
    }

    private fun handlePickedImagesResult(
        imageUris: List<Uri>?,
        onPickImagesSuccess: (imageUris: List<Uri>) -> Unit,
    ) {
        if (!imageUris.isNullOrEmpty()) {
            onPickImagesSuccess(imageUris)
        } else {
            onCancel()
        }
    }
}