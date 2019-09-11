package ib.ganz.myquran.kotlinstuff

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import ib.ganz.myquran.R

import java.util.regex.Pattern

//------------------------------ VIEW ------------------------------//

fun Any.clickOf(vararg v: View, f: () -> Unit) = v.forEach { it.setOnClickListener { f() } }

infix fun View.click(block: () -> Unit) = setOnClickListener { block() }

fun View.click(block: () -> Unit, bool: () -> Boolean) = setOnClickListener {
    when {bool() -> block()}
}

fun View.beVisible() {
    visibility = View.VISIBLE
}

infix fun View.beVisible(b: Boolean?) {
    if (b == true) visibility = View.VISIBLE
}

fun View.beInvisible() {
    visibility = View.INVISIBLE
}

infix fun View.beInvisible(b: Boolean?) {
    if (b == true) visibility = View.INVISIBLE
}

fun View.beGone() {
    visibility = View.GONE
}

infix fun View.beGone(b: Boolean?) {
    if (b == true) visibility = View.GONE
}

fun View.toggleVisibility(b: Boolean?) {
    visibility = if (b == true) View.VISIBLE else View.GONE
}

//------------------------------ IMAGEVIEW ------------------------------//

infix fun ImageView.useImage(id: Int) = setImageResource(id)

infix fun ImageView.useImage(b: Bitmap) = setImageBitmap(b)

infix fun ImageView.useImage(d: Drawable) = setImageDrawable(d)

fun ImageView.loadAsset(f: () -> String) = getBitmapFromAsset(f)?.let { useImage(it) }

//fun ImageView.loadImage(c: Context, s: String) {
//    Picasso.with(c).load(ApiClient.IMG_DIR + s).into(this)
//}

//------------------------------ TEXTVIEW ------------------------------//

fun TextView.isEmpty(): Boolean = text.toString().trim().isEmpty()

fun TextView.notEmpty(): Boolean = !isEmpty()

fun TextView.textIs(s: String): Boolean = text == s

infix fun TextView.text(s: String) {
    text = s
}

fun TextView.str() = text.toString().trim()

//------------------------------ EDITTEXT ------------------------------//

infix fun EditText.isValid(msg: String): Boolean {
    error = null
    if (isEmpty()) {
        requestFocus()
        error = msg
    }
    return !isEmpty()
}

fun EditText.isEmailValid(): Boolean {

    val email = str()
    val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(email)

    error = null

    if (!matcher.matches()) {
        requestFocus()
        error = "Email tidak valid"
        return false
    }

    return true
}

infix fun EditText.focusColor(color: Int) {

    val c = context

    if (!this.text.toString().trim().isEmpty()) {
        DrawableCompat.setTint(this.compoundDrawables[0], ContextCompat.getColor(c, color))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.elevation = 10f
        }
    } else {
        DrawableCompat.setTint(this.compoundDrawables[0], ContextCompat.getColor(c, R.color.textDarkDisabled))
    }

    this.setOnFocusChangeListener { _, b ->
        DrawableCompat.setTint(this.compoundDrawables[0], ContextCompat.getColor(c,
            if (b || !this.text.toString().trim { it <= ' ' }.isEmpty()) color else R.color.textDarkDisabled))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.elevation = (if (b) 10 else 4).toFloat()
        }
    }
}

//------------------------------ VIEWPAGER ------------------------------//

fun ViewPager.notAtFirst() = currentItem > 0

fun ViewPager.nextPage() {
    currentItem++
}

fun ViewPager.prefPage() {
    currentItem--
}