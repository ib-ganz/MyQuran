package ib.ganz.myquran.kotlinstuff

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.WindowManager
import com.google.gson.Gson
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import android.graphics.BitmapFactory
import android.util.Base64
import ib.ganz.myquran.helper.Gxon
import android.graphics.Rect


fun (() -> Unit).doIf(b: Boolean) {
    b.then { this() }
}

inline infix fun <T> T?.or(block: T?.() -> T): T = block()

inline infix fun Boolean?.then(block: () -> Unit): Boolean? {
    if (this == true) block()
    return this
}

inline infix fun Boolean?.otherwise(block: () -> Unit) {
    if (this == false) block()
}

infix fun <T> MutableList<T>.replaceWith(ts: MutableList<T>): MutableList<T> {
    clear()
    addAll(ts)
    return this
}

infix fun <T, R: RecyclerView.ViewHolder> MutableList<T>.notify(adp: RecyclerView.Adapter<R>) {
    adp.notifyDataSetChanged()
}

infix fun RecyclerView.use(lm: RecyclerView.LayoutManager): RecyclerView {
    layoutManager = lm
    return this
}

infix fun <T: RecyclerView.ViewHolder> RecyclerView.use(adp: RecyclerView.Adapter<T>): RecyclerView {
    adapter = adp
    return this
}

infix fun RecyclerView.use(i: RecyclerView.ItemDecoration): RecyclerView {
    addItemDecoration(i)
    return this
}

fun Activity.beFullScreen() = window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

fun Activity.imageOverlapStatusbar() = window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

fun Intent.onlyOne(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
    return this
}

fun Intent.recreate(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
    return this
}

fun Any.toJsonObject() = Gxon.toJsonObject(this)

fun <T> String.fromJsonObject(clazz: Class<T>) = Gson().fromJson(this, clazz)

fun <T> MutableList<T>.toJsonArray(): String {
    return Gson().toJson(this)
}

fun <T> String.fromJsonArray(c: Class<T>): MutableList<T> {
    return Gson().fromJson(this, ListOfJson(c))
}

fun String.toBitmap(): Bitmap {
    val decodedBytes = Base64.decode(this.substring(this.indexOf(",") + 1), Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

private class ListOfJson<T> internal constructor(wrapper: Class<T>) : ParameterizedType {
    private val wrapped: Class<*>

    init {
        this.wrapped = wrapper
    }

    override fun getActualTypeArguments(): Array<Type> {
        return arrayOf(wrapped)
    }

    override fun getRawType(): Type {
        return List::class.java
    }

    override fun getOwnerType(): Type? {
        return null
    }
}

fun Int.toArabicNumber(): String = toString().toArabicNumber()

fun String.toArabicNumber(): String {
    val arabicChars = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
    val builder = StringBuilder()
    for (i in 0 until this.length) {
        if (Character.isDigit(this[i])) {
            builder.append(arabicChars[this[i].toInt() - 48])
        } else {
            builder.append(this[i])
        }
    }
    return builder.toString()
}

fun Activity.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
//    val rectangle = Rect()
//    this.window.apply { decorView.getWindowVisibleDisplayFrame(rectangle) }
//    return rectangle.top
}