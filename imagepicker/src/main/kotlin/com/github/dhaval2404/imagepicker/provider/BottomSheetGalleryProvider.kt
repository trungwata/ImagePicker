package com.github.dhaval2404.imagepicker.provider

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePickerActivity
import com.github.dhaval2404.imagepicker.R

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
        appImagePicker.pickImage(maxImagesNum)
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
