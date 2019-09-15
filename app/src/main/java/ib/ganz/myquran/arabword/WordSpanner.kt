package ib.ganz.myquran.arabword

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.style.BackgroundColorSpan
import ib.ganz.myquran.R
import ib.ganz.myquran.database.AyatData
import ib.ganz.myquran.kotlinstuff.toArabicNumber
import java.util.regex.Pattern

class WordSpanner(private val ayatData: AyatData) {

    var words = mutableListOf<String>()

    fun setSpan(c: Context, f: String, a: String, l: String, form: String) {
        val reg = QueryRegex.generate(f, a, l, form)
        val pattern = Pattern.compile(reg)
        val matcher = pattern.matcher(ayatData.text)

        val lStartEndIndex = mutableListOf<Pair<Int, Int>>()

        while (matcher.find()) {
            words.add(matcher.group())
            lStartEndIndex.add(Pair(matcher.start(), matcher.end()))
        }

        ayatData.spannableText.apply {
            append(ayatData.text)
            lStartEndIndex.forEach {
                setSpan(BackgroundColorSpan(ContextCompat.getColor(c, R.color.colorMarineTrans)), it.first, it.second, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            }
            append("\uFD3F${ayatData.nomorAyat.toArabicNumber()}\uFD3E")
        }
    }

    class Found(val noVowelIndex: Int, val vowelIndex: Int = 0, var valid: Boolean = false)
}