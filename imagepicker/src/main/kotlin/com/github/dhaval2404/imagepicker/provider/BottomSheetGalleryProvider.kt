package com.github.dhaval2404.imagepicker.provider

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePickerActivity
import com.github.dhaval2404.imagepicker.R
import com.github.dhaval2404.imagepicker.util.PermissionUtil

/**
 * Select image from Storage
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2019
 */
class BottomSheetGalleryProvider(activity: ImagePickerActivity) :
    BaseProvider(activity) {

    // Mime types restrictions for gallery. By default all mime types are valid
    private val mimeTypes: Array<String>
    private val multiplePicker: Boolean
    private val maxImagesNum: Int
    private val appImagePicker: AppImagePicker

    init {
        val bundle = activity.intent.extras ?: Bundle()

        // Get MIME types
        mimeTypes = bundle.getStringArray(ImagePicker.EXTRA_MIME_TYPES) ?: emptyArray()
        multiplePicker = bundle.getBoolean(ImagePicker.EXTRA_MULTIPLE_PICKER, false)
        maxImagesNum =
            bundle.getInt(ImagePicker.EXTRA_MAX_IMAGES_NUM, ImagePicker.DEFAULT_MAX_IMAGES_NUM)
        appImagePicker = AppImagePicker(
            activity, {
                handleResult(it)
            },
            onCancel = {
                setResultCancel()
            }
        )
    }


    /**
     * Start Gallery Capture Intent
     */
    fun startIntent() {
        startGalleryIntent()
    }

    /**
     * Start Gallery Intent
     */
    private fun startGalleryIntent() {
        if (PermissionUtil.isPhotoPermissionGranted(this)){
            appImagePicker.pickImage(maxImagesNum)
        } else {
            requestPhotoPermission()
        }
    }

    private fun requestPhotoPermission(){
        // Permission request logic
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED, WRITE_EXTERNAL_STORAGE))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, WRITE_EXTERNAL_STORAGE))
        } else {
            requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE))
        }
    }

    // Android request photo permission
    // Register ActivityResult handler
    private val requestPermissions = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        startGalleryIntent()
    }

    private fun handleResult(imageUris: List<Uri>?) {
        if (imageUris.isNullOrEmpty()) {
            setError(R.string.error_failed_pick_gallery_image)
            return
        }
        imageUris.forEach {
            takePersistableUriPermission(it)
        }
        if (imageUris.size == 1) {
            activity.setImage(imageUris.first())
        } else {
            activity.setMultipleImages(imageUris)
        }
    }

    /**
     * Take a persistable URI permission grant that has been offered. Once
     * taken, the permission grant will be remembered across device reboots.
     */
    private fun takePersistableUriPermission(uri: Uri) {
        val name = activity.packageName
        activity.grantUriPermission(name, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)    }

}
