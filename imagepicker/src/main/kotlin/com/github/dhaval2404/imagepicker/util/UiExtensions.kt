package com.github.dhaval2404.imagepicker.util

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

fun ActivityResultLauncher<PickVisualMediaRequest>.lunchPickMultipleImages(){
    launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
}
