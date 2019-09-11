package ib.ganz.myquran.database

class QuranData(
    val text: String = "",
    val nomorAyat: String = "",
    val isHeader: Boolean = false,
    val nomorSurat: String = "",
    val namaSurat: String = "",
    val jumlahAyat: String = ""
) {
    companion object {
        fun fromListAyat(l: MutableList<AyatData>): MutableList<QuranData> {
            val lQuran = mutableListOf<QuranData>()
            l.forEach {
                if (it.nomorAyat == "1") {
                    lQuran.add(QuranData(
                        isHeader    = true,
                        nomorSurat  = it.idSurat,
                        namaSurat   = it.namaSurat,
                        jumlahAyat  = it.jumlahAyat
                    ))
                }
                lQuran.add(
                    QuranData(
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
    }
}