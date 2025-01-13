package com.github.dhaval2404.imagepicker.util

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * Permission utility class
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2019
 */
object PermissionUtil {

    /**
     * Check if Permission is granted
     *
     * @return true if specified permission is granted
     */
    fun isPermissionGranted(context: Context, permission: String): Boolean {
        val selfPermission = ContextCompat.checkSelfPermission(context, permission)
        return selfPermission == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Check if Specified Permissions are granted or not. If single permission is denied then
     * function will return false.
     *
     * @param context Application Context
     * @param permissions Array of Permission to Check
     *
     * @return true if all specified permission is granted
     */
    fun isPermissionGranted(context: Context, permissions: Array<String>): Boolean {
        return permissions.filter {
            isPermissionGranted(context, it)
        }.size == permissions.size
    }

    /**
     * Check if Specified Permission is defined in AndroidManifest.xml file or not.
     * If permission is defined in manifest then return true else return false.
     *
     * @param context Application Context
     * @param permission String Permission Name
     *
     * @return true if permission defined in AndroidManifest.xml file, else return false.
     */
    fun isPermissionInManifest(context: Context, permission: String): Boolean {
        val packageInfo = context.packageManager.getPackageInfo(
            context.packageName, PackageManager.GET_PERMISSIONS
        )
        val permissions = packageInfo.requestedPermissions

        if (permissions.isNullOrEmpty()) return false

        for (perm in permissions) {
            if (perm == permission) return true
        }

        return false
    }

    fun isPhotoPermissionGranted(context: Context): Boolean {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && (ContextCompat.checkSelfPermission(
                context, READ_MEDIA_IMAGES
            ) == PERMISSION_GRANTED) -> {
            // Full access on Android 13 (API level 33) or higher
            return true
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE && return ContextCompat.checkSelfPermission(
                context, READ_MEDIA_VISUAL_USER_SELECTED
            ) == PERMISSION_GRANTED -> {
                // Partial access on Android 14 (API level 34) or higher
                return true
            }

            ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED -> {
                // Full access up to Android 12 (API level 32)
                return true
            }
            else -> {
                // Access denied
                return false
            }
        }
    }

}
