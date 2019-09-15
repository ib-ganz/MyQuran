package ib.ganz.myquran.arabword

import android.content.Context
import ib.ganz.myquran.database.AyatData
import java.lang.Exception

object WordProcessor {

    class Wrapper {

        private var onSuccess: ((MutableList<AyatData>, Int) -> Unit)? = null
        private var onError: ((String) -> Unit)? = null

        fun onSuccess(f: (MutableList<AyatData>, Int) -> Unit) {
            onSuccess = f
        }

        fun onError(f: (String) -> Unit) {
            onError = f
        }

        fun doSuccess(l: MutableList<AyatData>, count: Int) {
            onSuccess?.invoke(l, count)
        }

        fun doError(s: String) {
            onError?.invoke(s)
        }
    }

    fun process(c: Context, s: String, fw: Wrapper.() -> Unit) {

        val w = Wrapper().apply(fw)

        var wordCount = 0
        var f = ""
        var a = ""
        var l = ""

        try {
            f = s[0].toString()
            a = s[1].toString()
            l = s[2].toString()
        }
        catch (e: Exception) {
            w.doError("Format kata salah")
        }

        val lAyat = AyatData.getRegex(c, f, a, l, "")
        lAyat.forEach { ayatData ->
            val ws = WordSpanner(ayatData).apply { setSpan(c, f, a, l, "") }
            wordCount += ws.words.size
        }

        w.doSuccess(lAyat, wordCount)
    }

    fun old1process(c: Context, s: String, fw: Wrapper.() -> Unit) {

        val w = Wrapper().apply(fw)

        var wordCount = 0
        var f = ""
        var a = ""
        var l = ""

        try {
            f = s[0].toString()
            a = s[1].toString()
            l = s[2].toString()
        }
        catch (e: Exception) {
            w.doError("Format kata salah")
        }

        val lForms = getAllForms(f, a, l)
        val lAyat = mutableListOf<AyatData>()

        lForms.forEach { form ->
            val r = AyatData.getLike(c, form)
            r.forEach { ayatData ->
                val ws = WordSpanner(ayatData).apply { setSpan(c, f, a, l, form) }
                wordCount += ws.words.size
            }
            lAyat.addAll(r)
        }

        w.doSuccess(lAyat, wordCount)
    }

    private fun getAllForms(f: String, a: String, l: String): MutableList<String> {
        val madhi = mutableListOf(
            "$f$a$l",  // ف ع ل
            "$f${a}ّ$l", // ف ع ع ل
            "${f}ا$a$l", // ف ا ع ل
            "${f}ت$a$l", // ف ت ع ل
            "$f$a${l}ّ" // ف ع ل ل
        )
        val mashdar = mutableListOf(
            "$f${a}ي$l", // ف ع ي ل
            "$f${a}ا$l", // ف ع ا ل
            "${f}ت${a}ا$l" // ف ت ع ا ل
        )
        return madhi.apply { addAll(mashdar) }
    }
}