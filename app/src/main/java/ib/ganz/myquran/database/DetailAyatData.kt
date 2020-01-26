package ib.ganz.myquran.database

import android.content.Context

class DetailAyatData {
    companion object {
        // Pair<String, String> = terjemah, tafsir
        fun getDetail(c: Context, idAyat: String): Pair<String, String> {
            val res = SQLiteDataManager.readRow(c,
                    "SELECT t.AyahText as terjemah, tj.text as tafsir FROM terjemah t " +
                    "JOIN tafsir_jalalain tj ON t.id_terjemah = tj.id_tafsir " +
                    "JOIN ayat a ON a.id_ayat = t.id_terjemah " +
                    "WHERE a.id_ayat = '$idAyat'"
            )
            return Pair(res[0], res[1])
        }
    }
}