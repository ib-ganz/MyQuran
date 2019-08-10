package ib.ganz.sipp_on.kotlinstuff

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
import ib.ganz.myquran.halper.Gxon

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