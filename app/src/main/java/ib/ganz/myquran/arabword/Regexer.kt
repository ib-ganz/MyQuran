package ib.ganz.myquran.arabword

object Regexer {

    private const val tsd = "ّ"
    private const val alf = "ا"
    private const val alfWasl = "ٱ"
    private const val alfMaq = "ى"
    private const val alfMaq2 = "ی"
    private const val ya = "ي"
    private const val waw = "و"

    private const val hmz1 = "ء"
    private const val hmz2 = "آ"
    private const val hmz3 = "أ"
    private const val hmz4 = "ؤ"
    private const val hmz5 = "إ"
    private const val hmz6 = "ئ"
    private const val hmz7 = "ٔ"
    private const val hmz8 = "ٕ"

    fun generate(f: String, a: String, l: String, form: String): String {

        var func: (String, String, String) -> String = RegexGenerator.sohih

        if (!f.isIllat() && !a.isIllat() && !l.isIllat() && !f.isHamzah() && !a.isHamzah() && !l.isHamzah()) {
            func = if (a == l || l == tsd) {
                RegexGenerator.mudoaf
            } else {
                RegexGenerator.sohih
            }
        }
        else if (f.isIllat() || a.isIllat() || l.isIllat()) {
            if ((f.isIllat() && l.isIllat()) || (a.isIllat() && l.isIllat())) {
                func = RegexGenerator.lafif
            }
            else if (f.isIllat()) {
                func = RegexGenerator.misal
            }
            else if (a.isIllat()) {
                func = RegexGenerator.ajwaf
            }
            else if (l.isIllat()) {
                func = RegexGenerator.naqis
            }
        }
        else if (f.isHamzah()) {
            func = RegexGenerator.mahmuzFa
        }
        else if (a.isHamzah()) {
            func = RegexGenerator.mahmuzAin
        }
        else if (l.isHamzah()) {
            func = RegexGenerator.mahmuzLam
        }

        return func(f, a, l)
    }

    private fun String.isHamzah(): Boolean {
        return this == hmz1 || this == hmz2 || this == hmz3 || this == hmz4 || this == hmz5 || this == hmz6
    }

    private fun String.isIllat(): Boolean {
        return this == ya || this == waw || this == alf || this == alfMaq || this == alfMaq2
    }
}