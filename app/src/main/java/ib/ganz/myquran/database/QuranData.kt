package ib.ganz.myquran.database

class QuranData(
    val idAyat: String = "",
    val text: String = "",
    val nomorAyat: String = "",
    val isHeader: Boolean = false,
    val nomorSurat: String = "",
    val namaSurat: String = "",
    val jumlahAyat: String = "",
    val terjemah: String = "",
    val tafsirJalalain: String = ""
) {
    companion object {
        fun fromListAyat(l: MutableList<AyatData>): MutableList<QuranData> {
            val lQuran = mutableListOf<QuranData>()
            l.forEach {
                if (it.nomorAyat == "1") {
                    lQuran.add(QuranData(
                        idAyat      = it.idAyat,
                        isHeader    = true,
                        nomorSurat  = it.idSurat,
                        namaSurat   = it.namaSurat,
                        jumlahAyat  = it.jumlahAyat
                    ))
                }
                lQuran.add(
                    QuranData(
                        idAyat      = it.idAyat,
                        text        = it.text.replace("بِسْمِ ٱللَّهِ ٱلرَّحْمٰنِ ٱلرَّحِيمِ ", ""),
                        nomorAyat   = it.nomorAyat,
                        isHeader    = false,
                        nomorSurat  = it.idSurat,
                        namaSurat   = it.namaSurat,
                        jumlahAyat  = it.jumlahAyat
                    )
                )
            }
            return lQuran
        }

        val NOTHING = -1
        val LOADING = 0
        val PLAYING = 1
        val PAUSED = 2
    }

    var state = NOTHING
}