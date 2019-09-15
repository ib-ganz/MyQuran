package ib.ganz.myquran.database

import android.content.Context
import android.text.SpannableStringBuilder
import ib.ganz.myquran.arabword.QueryRegex
import java.util.regex.Pattern

class AyatData(s: Array<String>) {

    companion object {
        fun getFatihah(c: Context): MutableList<AyatData> {
            val res = SQLiteDataManager.read(c, "SELECT * FROM ayat a JOIN surat s ON a.id_surat = s.id_surat WHERE a.id_surat >= 78")
            val l = mutableListOf<AyatData>()
            res.forEach {
                l.add(AyatData(it))
            }
            return l
        }

        fun getFromVoice(c: Context, ss: List<String>): MutableList<AyatData> {
            val l = mutableListOf<AyatData>()
            for (s in ss) {
                val q = "SELECT * FROM ayat a JOIN surat s ON a.id_surat = s.id_surat WHERE a.mentahan LIKE '%$s%'"
                SQLiteDataManager.read(c, q)?.forEach {
                    l.add(AyatData(it))
                }
            }
            return l
        }

        fun getLike(c: Context, s: String): MutableList<AyatData> {
            val l = mutableListOf<AyatData>()
            val q = "SELECT * FROM ayat a JOIN surat s ON a.id_surat = s.id_surat WHERE a.mentahan LIKE '%$s%'"
            SQLiteDataManager.read(c, q)?.forEach {
                l.add(AyatData(it))
            }
            return l
        }

        fun getRegex(c: Context, f: String, a: String, l: String, form: String): MutableList<AyatData> {
            val list = mutableListOf<AyatData>()
            val q = "SELECT * FROM ayat a JOIN surat s ON a.id_surat = s.id_surat WHERE a.text LIKE '%$f%' AND a.text LIKE '%$a%' AND a.text LIKE '%$l%'"
            val reg = QueryRegex.generate(f, a, l, form)

            SQLiteDataManager.read(c, q)?.forEach {
                val matcher = Pattern.compile(reg).matcher(it[3])
                if (matcher.find()) {
                    list.add(AyatData(it))
                }
            }
            return list
        }
    }

    var idAyat: String = s[0]
    var idSurat: String = s[1]
    var nomorAyat: String = s[2]
    var text: String = s[3]
    var mentahan: String = s[4]
    var namaSurat = s[6]
    var jumlahAyat = s[7]
    var spannableText = SpannableStringBuilder()
}