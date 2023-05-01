package ru.android.hikanumaruapp.data

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.jar.Manifest

class PermissionAdapter(private val activity: Activity) {

    companion object {
        private const val REQUEST_WRITE_EXTERNAL_STORAGE = 1
    }

    fun requestStoragePermission() {
        // Проверяем, есть ли у нас уже разрешение на запись во внешнее хранилище
        if (ContextCompat.checkSelfPermission(activity,
                WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Если разрешение еще не было предоставлено, запрашиваем его у пользователя
            ActivityCompat.requestPermissions(activity,
                arrayOf(WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_EXTERNAL_STORAGE)
        }
    }

    fun handleStoragePermissionRequestResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        return when (requestCode) {
            // Если пользователь предоставил разрешение, вернем true
            REQUEST_WRITE_EXTERNAL_STORAGE ->
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            else -> false
        }
    }
}