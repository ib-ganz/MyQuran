package ib.ganz.myquran.kotlinstuff.extfun

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.WindowManager
import android.widget.Toast

fun Activity.beFullScreen() = window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

fun Activity.imageOverlapStatusbar() = window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)


//fun Activity.dialog(layout: Int, f: (MyDialogue.() -> Unit)? = null) = MyDialogue.dialog(this, layout, f)

fun Context.toast(f: () -> String) = Toast.makeText(this, f(), Toast.LENGTH_SHORT).show()

fun Context.toast(s: String) = Toast.makeText(this, s, Toast.LENGTH_SHORT).show()

fun Context.d(f: () -> String) = Log.d("logqu", f())


fun Activity.getDefaultImgRequestCode() = 666

fun Activity.getDefaultVidRequestCode() = 555

fun Activity.getDefaultPermissionRequestCode() = 999

fun Activity.getDefaultStoragePermissionRequestCode() = 777

fun Activity.getDefaultLocationPermissionRequestCode() = 13

fun Activity.hasPermissions(permissions: Array<String>): Boolean {
    for (permission in permissions) {
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}

fun Activity.requestPermissions(permissions: Array<String>, permissionCode: Int = getDefaultPermissionRequestCode(), onHasPermission: (() -> Unit)? = null) {
    if (hasPermissions(permissions)) {
        onHasPermission?.invoke()
    } else {
        ActivityCompat.requestPermissions(this, permissions, permissionCode)
    }
}

fun Activity.requestStoragePermission(onHasPermission: (() -> Unit)? = null) {
    requestPermissions(
        arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ),
        permissionCode = getDefaultStoragePermissionRequestCode(),
        onHasPermission = onHasPermission
    )
}

fun Activity.requestLocationPermission(onHasPermission: (() -> Unit)? = null) {
    requestPermissions(
        arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ),
        permissionCode = getDefaultLocationPermissionRequestCode(),
        onHasPermission = onHasPermission
    )
}

fun Activity.reqStorageAndLocationPermissions(onHasPermission: (() -> Unit)? = null) {
    requestPermissions(
        arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ),
        permissionCode = getDefaultLocationPermissionRequestCode(),
        onHasPermission = onHasPermission
    )
}