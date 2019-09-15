package ib.ganz.myquran.customclass

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.ReplacementSpan
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import ib.ganz.myquran.R


class RoundedBackgroundSpan(context: Context) : ReplacementSpan() {

    private val CORNER_RADIUS = 8
    private var backgroundColor = 0
    private var textColor = 0

    init {
        backgroundColor = ContextCompat.getColor(context, R.color.colorMarineTrans)
        textColor = ContextCompat.getColor(context, android.R.color.tab_indicator_text)
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val rect = RectF(x, top.toFloat(), x + measureText(paint, text, start, end), bottom.toFloat())
        paint.color = backgroundColor
        canvas.drawRoundRect(rect, CORNER_RADIUS.toFloat(), CORNER_RADIUS.toFloat(), paint)
        paint.color = textColor
        canvas.drawText(text, start, end, x, y.toFloat(), paint)
    }

    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        return Math.round(paint.measureText(text, start, end))
    }

    private fun measureText(paint: Paint, text: CharSequence, start: Int, end: Int): Float {
        return paint.measureText(text, start, end)
    }

}