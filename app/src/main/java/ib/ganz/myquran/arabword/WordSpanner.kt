package ib.ganz.myquran.arabword

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.style.BackgroundColorSpan
import ib.ganz.myquran.R
import ib.ganz.myquran.database.AyatData
import ib.ganz.myquran.kotlinstuff.toArabicNumber
import java.lang.Exception
import java.util.regex.Pattern

class WordSpanner(private val ayatData: AyatData, private val regex: String) {

    var words = mutableListOf<String>()

    suspend fun setSpan(c: Context) {
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(ayatData.text)

        val lStartEndIndex = mutableListOf<Pair<Int, Int>>()

        while (matcher.find()) {
            try {
                words.add(matcher.group(1))
                lStartEndIndex.add(Pair(matcher.start(1), matcher.end(1)))
             }
            catch (e: Exception) {
                try {
                    words.add(matcher.group(2))
                    lStartEndIndex.add(Pair(matcher.start(2), matcher.end(2)))
                }
                catch (e: Exception) {
                    words.add(matcher.group())
                    lStartEndIndex.add(Pair(matcher.start(), matcher.end()))
                }
            }
        }

        ayatData.spannableText.apply {
            append(ayatData.text)
            lStartEndIndex.forEach {
                setSpan(BackgroundColorSpan(ContextCompat.getColor(c, R.color.colorMarineTrans)), it.first, it.second, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            }
            append("\uFD3F${ayatData.nomorAyat.toArabicNumber()}\uFD3E")
        }
    }
}