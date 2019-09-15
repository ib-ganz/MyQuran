package ib.ganz.myquran.arabword

object QueryRegex {

    val fth = "َ"
    val ksr = "ِ"
    val dmm = "ُ"
    val skn = "ْ"
    val mad = "ٰ"
    val tsd = "ّ"

    val alif = "ا"
    val lam = "ل"
    val ta = "ت"
    val ya = "ي"
    val waw = "و"
    val nun = "ن"
    val fa = "ن"

    val hmz1 = "ء"
    val hmz2 = "آ"
    val hmz3 = "أ"
    val hmz4 = "ؤ"
    val hmz5 = "إ"
    val hmz6 = "ئ"

    fun generate(fFiil: String, aFiil: String, lFiil: String, form: String): String {
        val f = fFiil
        val a = aFiil
        val l = lFiil

        return "$f$tsd?[$fth$ksr$dmm$skn$mad]?$alif?(?:$ta[$fth$ksr$dmm])?" +
                "$a$tsd?[$fth$ksr$dmm$skn]?[$waw$ya$alif]?$skn?$l"
    }
}

// ف ع ل
// ف ع ع ل
// ف ا ع ل
// ف ت ع ل
// ف ع ل ل
// ف ع ي ل
// ف ع و ل
// ف ع ا ل
// ف ا ع ي ل
// ف ع ع ا ل
// ف ت ع ا ل





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