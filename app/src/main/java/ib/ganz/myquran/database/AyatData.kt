package ib.ganz.myquran.database

import android.content.Context
import android.text.SpannableStringBuilder
import java.util.regex.Pattern

class AyatData(
    var idAyat: String,
    var idSurat: String,
    var nomorAyat: String,
    var text: String,
    var mentahan: String,
    var namaSurat: String,
    var jumlahAyat: String,
    var tafsir: String
) {

    companion object {
        fun getAll(c: Context): MutableList<AyatData> {
            val res = SQLiteDataManager.read(c,
                "SELECT a.*, s.*, t.text FROM ayat a " +
                "JOIN surat s ON a.id_surat = s.id_surat " +
                "JOIN tafsir_jalalain t ON t.id_tafsir = a.id_ayat " +
                "WHERE 1"
            )
            val l = mutableListOf<AyatData>()
            res.forEach {
                l.add(AyatData(it))
            }
            return l
        }

        fun getFromVoice(c: Context, ss: List<String>): MutableList<AyatData> {
            val l = mutableListOf<AyatData>()
            for (s in ss) {
                val q =
                    "SELECT a.id_ayat, a.id_surat, a.nomor_ayat, a.text, a.mentahan, s.*, t.text " +
                    "FROM ayat a " +
                    "JOIN surat s ON a.id_surat = s.id_surat " +
                    "JOIN tafsir_jalalain t ON t.id_tafsir = a.id_ayat " +
                    "WHERE a.no_hamza LIKE '%$s%'"
                SQLiteDataManager.read(c, q)?.forEach {
                    val k = l.find { y -> y.idAyat == it[0] }
                    if (k == null) l.add(AyatData(it))
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

        fun getByRegex(c: Context, f: String, a: String, l: String, regex: String): MutableList<AyatData> {
            val list = mutableListOf<AyatData>()
            val q = "SELECT a.id_ayat, a.id_surat, a.nomor_ayat, a.text, a.mentahan, s.*, t.text " +
                    "FROM ayat a " +
                    "JOIN surat s ON a.id_surat = s.id_surat " +
                    "JOIN tafsir_jalalain t ON t.id_tafsir = a.id_ayat"

            SQLiteDataManager.read(c, q)?.forEach {
                val matcher = Pattern.compile(regex).matcher(it[3])
                if (matcher.find()) {
                    list.add(AyatData(it))
                }
            }
            return list
        }

        fun getByIdSurat(c: Context, idSurat: String): MutableList<AyatData> {
            val l = mutableListOf<AyatData>()
            val q =
                "SELECT a.id_ayat, a.id_surat, a.nomor_ayat, a.text, a.mentahan, s.*, t.text " +
                        "FROM ayat a " +
                        "JOIN surat s ON a.id_surat = s.id_surat " +
                        "JOIN tafsir_jalalain t ON t.id_tafsir = a.id_ayat " +
                        "WHERE a.id_surat LIKE '$idSurat'"
            SQLiteDataManager.read(c, q)?.forEach {
                l.add(AyatData(it))
            }
            return l
        }

        fun getByIdAyat(c: Context, idAyat: String): AyatData {
            val q =
                "SELECT a.id_ayat, a.id_surat, a.nomor_ayat, a.text, a.mentahan, s.*, t.text " +
                "FROM ayat a " +
                "JOIN surat s ON a.id_surat = s.id_surat " +
                "JOIN tafsir_jalalain t ON t.id_tafsir = a.id_ayat " +
                "WHERE a.id_ayat = '$idAyat'"
            val r = SQLiteDataManager.readRow(c, q)
            return AyatData(r)
        }

        fun getBismillah(c: Context): AyatData {
            val q =
                "SELECT a.id_ayat, a.id_surat, a.nomor_ayat, a.text, a.mentahan, s.*, t.text " +
                "FROM ayat a " +
                "JOIN surat s ON a.id_surat = s.id_surat " +
                "JOIN tafsir_jalalain t ON t.id_tafsir = a.id_ayat " +
                "WHERE a.id_ayat = 1"
            val r = SQLiteDataManager.readRow(c, q)
            return AyatData(r)
        }
    }

    var spannableText = SpannableStringBuilder()

    constructor(s: Array<String>) : this(
        idAyat = s[0],
        idSurat = s[1],
        nomorAyat = s[2],
        text = s[3],
        mentahan = s[4],
        namaSurat = s[6],
        jumlahAyat = s[7],
        tafsir = s[8]
    )
}