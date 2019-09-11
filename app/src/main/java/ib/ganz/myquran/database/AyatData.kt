package ib.ganz.myquran.database

import android.content.Context

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
    }

    var idAyat: String = s[0]
    var idSurat: String = s[1]
    var nomorAyat: String = s[2]
    var text: String = s[3]
    var mentahan: String = s[4]
    var namaSurat = s[6]
    var jumlahAyat = s[7]
}