package ib.ganz.myquran.arabword

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.style.BackgroundColorSpan
import ib.ganz.myquran.R
import ib.ganz.myquran.database.AyatData
import ib.ganz.myquran.kotlinstuff.toArabicNumber

class Old1WordSpanner(private val ayatData: AyatData) {

    var wordCount = 0

    private val noVowelText = ayatData.mentahan
    private val vowelText = ayatData.text
    private val fIndexes = mutableListOf<FIndex>()

    fun setSpan(c: Context, f: String, a: String, l: String, form: String) {
        var noVowelIdx = noVowelText.indexOf(f)
        var vowelIdx = vowelText.indexOf(f)

        while (vowelIdx >= 0 && vowelIdx < vowelText.length - 2) {
            fIndexes.add(FIndex(noVowelIdx, vowelIdx))
            noVowelIdx = noVowelText.indexOf(f, noVowelIdx + 1)
            vowelIdx = vowelText.indexOf(f, vowelIdx + 1)
        }

        val lPair = mutableListOf<Pair<Int, Int>>()

        fIndexes.forEach {
            val sub = vowelText.substring(it.vowelIndex, vowelText.length)
            val aIdx = sub.indexOf(a)
            if (aIdx in 0..5) {
                lPair.add(Pair(it.vowelIndex, it.vowelIndex + sub.indexOf(l) + 1))
            }
        }

        ayatData.spannableText.apply {
            append(vowelText)
            lPair.forEach {
                wordCount++
                setSpan(BackgroundColorSpan(ContextCompat.getColor(c, R.color.colorMarineTrans)), it.first, it.second, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            }
            append("\uFD3F${ayatData.nomorAyat.toArabicNumber()}\uFD3E")
        }
    }

    class FIndex(val noVowelIndex: Int, val vowelIndex: Int = 0, var valid: Boolean = false)
}