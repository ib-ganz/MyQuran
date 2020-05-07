package ib.ganz.myquran.database

import android.content.Context

class SuratData(s: Array<String>) {

    companion object {
        fun getAll(c: Context): MutableList<SuratData> {
            val l = mutableListOf<SuratData>()
            SQLiteDataManager.read(c, "SELECT * FROM surat").forEach {
                l.add(SuratData(it))
            }
            return l
        }
        fun getByIdSurat(c: Context, idSurat: String): SuratData {
            val l = mutableListOf<SuratData>()
            val r = SQLiteDataManager.readRow(c, "SELECT * FROM surat WHERE id_surat = $idSurat")
            return SuratData(r)
        }
        fun getByIdAyat(c: Context, idAyat: String): SuratData {
            val l = mutableListOf<SuratData>()
            val r = SQLiteDataManager.readRow(c, "SELECT s.* FROM surat s JOIN ayat a ON a.id_surat = s.id_surat WHERE a.id_ayat = $idAyat")
            return SuratData(r)
        }
    }

    var idSurat: String = s[0]
    var nama: String = s[1]
    var jumlahAyat: String = s[2]
}