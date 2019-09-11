package ib.ganz.myquran.kotlinstuff

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import java.io.IOException
import java.io.InputStream

fun View.getBitmapFromAsset(filePath: () -> String): Bitmap? = context.getBitmapFromAsset(filePath)

fun Context.getBitmapFromAsset(filePath: () -> String): Bitmap? {
    val assetManager = this.assets
    val istr: InputStream
    var bitmap: Bitmap? = null
    try {
        istr = assetManager.open(filePath())
        bitmap = BitmapFactory.decodeStream(istr)
    }
    catch (e: IOException) {
        e.printStackTrace()
    }

    return bitmap
}