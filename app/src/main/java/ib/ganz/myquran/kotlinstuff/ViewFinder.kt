package ib.ganz.myquran.kotlinstuff

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.extensions.LayoutContainer

/**
 * Created by Nalpot on 07/03/2019.
 * Email: rifqim035@gmail.com
 */

class ViewHaver(override val containerView: View?) : LayoutContainer

fun Context.findViews(layout: Int, f: ViewHaver.() -> Unit) = ViewHaver(LayoutInflater.from(this).inflate(layout, null)).f()