package ib.ganz.myquran.kotlinstuff

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

fun Bitmap.scale(maxSize: Double = 300.0) : Bitmap {
    val ratio: Double
    var w = this.width.toDouble()
    var h = this.height.toDouble()

    if (w < maxSize || h < maxSize)
        return this

    if (w < h) {
        if (w > maxSize) {
            w = maxSize

            ratio = maxSize / this.width
            h *= ratio
        }
    } else if (w > h) {
        if (h > maxSize) {
            h = maxSize
            ratio = maxSize / this.height
            w *= ratio
        }
    } else {
        if (h > maxSize) {
            h = maxSize
            w = maxSize
        }
    }

    return if (w != this.width.toDouble() || h != this.height.toDouble())
        Bitmap.createScaledBitmap(this, w.toInt(), h.toInt(), false)
    else
        this
}

fun Bitmap.convertToString(): String {
    val b = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, b)
    val arB = b.toByteArray()
    return Base64.encodeToString(arB, Base64.DEFAULT)
}