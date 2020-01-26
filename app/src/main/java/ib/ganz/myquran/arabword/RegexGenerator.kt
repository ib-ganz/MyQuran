package ib.ganz.myquran.arabword

// QOWAID: mau 4

// DAFTAR BUG
// وَعَادٍ attaubah:70 cari wa'ada muncul
// al israa: 64 wa'ada belum nemu ya'idu
// maryam: 94 wa'ada ketemu wa-'addahum 'addaa

object RegexGenerator {

    // ------------------------------- DIACRITICS -------------------------------

    private const val fathah = "َ"
    private const val fathatan = "ً"
    private const val kasrah = "ِ"
    private const val kasratan = "ٍ"
    private const val dommah = "ُ"
    private const val dommatan = "ٌ"
    private const val sukun = "ْ"
    private const val sukunSm = "۟"
    private const val tasydid = "ّ"
    private const val madd = "ٰ"
    private const val madd6 = "ٓ"
    private const val madd6lg = "ۤ"

    private const val signMim1 = "ۘ"
    private const val signMim2 = "ۢ"
    private const val signMim3 = "ۭ"
    private const val signTriDots = "ۛ"
    private const val signHighSin = "ۜ"
    private const val signLowSin = "ۣ"
    private const val signRoundZero = "۟"
    private const val signRectZero = "۠"
    private const val signHak = "ۡ"
    private const val signWaw = "ۥ"
    private const val signYa1 = "ۦ"
    private const val signYa2 = "ۧ"
    private const val signNun = "ۨ"
    private const val signEmptyCtLS = "۪"
    private const val signEmptyCtHS = "۫"
    private const val signRoundHS = "۬"

    private const val diacritics =
        "۬$signMim1$signMim2$signMim3$signTriDots$signHighSin$signLowSin$signRoundZero$signRectZero" +
        "$signHak$signWaw$signYa1$signYa2$signNun$signEmptyCtLS$signEmptyCtHS$signRoundHS"
    private const val allHarakats = "$fathah$fathatan$kasrah$kasratan$dommah$dommatan$sukun$sukunSm$tasydid$madd$madd6$madd6lg"
    private const val primaryHarakat = "$fathah$kasrah$dommah$sukun$sukunSm"

    // ------------------------------- END DIACRITICS -------------------------------

    private const val alif = "ا"
    private const val alifWasl = "ٱ"
    private const val alifMaqsuroh = "ى"
    private const val alifMaqsuroh2 = "ی"
    private const val ya = "ي"
    private const val waw = "و"

    private const val ba = "ب"
    private const val kaf = "ك"
    private const val lam = "ل"
    private const val ta = "ت"
    private const val nun = "ن"

    private const val fa = "ف"
    private const val sin = "س"
    private const val mim = "م"
    private const val taMarbutoh = "ة"
    private const val ha = "ه"

    private const val hmz1 = "ء"
    private const val hmz2 = "آ"
    private const val hmz3 = "أ"
    private const val hmz4 = "ؤ"
    private const val hmz5 = "إ"
    private const val hmz6 = "ئ"
    private const val hmz7 = "ٔ"
    private const val hmz8 = "ٕ"
    private const val tatweel = "ـ"
    private const val hmzTat = "ـٔ"

    private const val jar = "$kaf$lam$ba$ta$waw"
    private const val mudhori = "$ya$ta$nun$hmz3"
    private const val etc = "$ta$nun$alif$sin$mim$fa$alifWasl"
    private const val hmzs = "$hmz1$hmz2$hmz3$hmz4$hmz5$hmz6$hmz7$hmz8"

    private const val hurufAwal = "$mudhori$jar$etc$allHarakats$diacritics"
    private const val hurufAkhir = "$alif$nun$waw$ya$alifMaqsuroh$alifMaqsuroh2$taMarbutoh$ta$ha$mim$kaf$hmzs$allHarakats$diacritics"

    // ------------------------------- TEMPLATE -------------------------------

    private const val pre =
        "(?:^|[ ])(?:[$hurufAwal]*)" +
        "(?<![$nun$ya$ta$sin$jar][$allHarakats][$fa$ba][$allHarakats])" + // eliminasi [fa,ba] di belakang y, n, t, s, jar
        "(?<!$mim$tasydid?[$allHarakats][^$sin$ta$nun][$allHarakats])" + // eliminasi yg m [^stn]
        "(?<!$mim$tasydid?[$allHarakats]$sin[^$sukun])" + // eliminasi m s ^sukun
        "(?<!$mim$tasydid?[$allHarakats]$sin$sukun[^$ta])" + // eliminasi m s ^ta
        "(?<!$kaf[^$fathah])" + // eliminasi kaf yg tdk fathah
        "(?<!$ba$sukun$ta[$fathah$kasrah$dommah])" + // eliminasi ba sukuun terus ta [hrkts], karena @BUG da'aa ketemu ibtada'uu
        "(?<!$ba$fathah$lam$fathah)" + // eliminasi ba yg tdk kasrah, karena @BUG ghaniya ketemu balaghaniy
        "(?<!$sin[$allHarakats])" + // eliminasi yg depannya s tok
        "(?<!$sin$tasydid[$allHarakats])" + // eliminasi yg depannya s tasydid tok
        "(?<!$sin[$allHarakats]$alif)" + // eliminasi yg depannya saa tok
        "(?<!$lam$madd)" // eliminasi lam madd

    private const val end =
        "(?![^ ]*$waw$tasydid[$allHarakats])" + // kasus -> وَعَدُوَّكُمْ
        "(?![^ ]*$mim$sukun[$hurufAkhir])" + // eliminasi semua huruf akhir setelah mim sukun; kasus -> cari ajrun ketemu ajramnaa
                // eliminasi di bawah ini dihapus karena amintum jadi hilang albaqarah 196
//        "(?![^ ]*$sukun*$mim[$allHarakats](?:[ ]|${'$'}))" + // eliminasi yg belakangnya sukun* mim [hrkts] trus akhir ayat
        "(?=[$hurufAkhir]*(?:[ ]|${'$'}))"

    // ------------------------------- END TEMPLATE -------------------------------

    val sohih = { f: String, a: String, l: String ->
        "$pre(" +
            "$f$tasydid?[$primaryHarakat$madd]?$alif?(?:$ta[$fathah$kasrah$dommah])?" +
            "$a$tasydid?[$primaryHarakat$madd]?[$waw$ya$alif]?$sukun?$l" +
        ")$end"
    }

    // --------------------------------------------
    // lamnya boleh ain boleh tasydid
    // --------------------------------------------
    // belum cek hamaa'im
    // --------------------------------------------
    val mudoaf = { f: String, a: String, _: String ->
        "$pre(?:" +
            "($f$tasydid?[$primaryHarakat$madd]?$alif?(?:$ta[$fathah$kasrah$dommah])?$a$tasydid?[$primaryHarakat$madd]?[$waw$ya$alif]?$sukun?$a)" +
            "|($f$tasydid?[$primaryHarakat$madd]?$alif?(?:$ta[$fathah$kasrah$dommah])?$a$tasydid)" +
        ")$end"
    }

    // tatweel durung beres : ajrun
    // belum cek madd 6 hrkt
    // cek ٱلْمُؤْتَفِكٰتِ
    // cek ittakhodza
    // --------------------------------------------
    val mahmuzFa = { f: String, a: String, l: String ->
        "$pre(" +
            "(?![$hmzs][$allHarakats]$a$tasydid)" +
            "[$hmzs]$tasydid?[$primaryHarakat$madd]?$alif?(?:$ta[$fathah$kasrah$dommah])?$a$tasydid?[$primaryHarakat$madd]?[$waw$ya$alif]?$sukun?$l" +
        ")$end"
    }
    val mahmuzAin = { f: String, a: String, l: String ->
        """$pre($f$tasydid?[$primaryHarakat$madd]?$alif?(?:$ta[$fathah$kasrah$dommah])?$a$tasydid?[$primaryHarakat$madd]?[$waw$ya$alif]?$sukun?$l)$end"""
    }
    val mahmuzLam = { f: String, a: String, l: String ->
        """$pre($f$tasydid?[$primaryHarakat$madd]?$alif?(?:$ta[$fathah$kasrah$dommah])?$a$tasydid?[$primaryHarakat$madd]?[$waw$ya$alif]?$sukun?$l)$end"""
    }

    // --------------------------------------------
    // lam nya ga boleh ada tasydidnya
    // --------------------------------------------
    // sudah bisa adhaa'a
    // bug -> yasara - asraa al anfal 67; nasraa nuh 23
    // bug -> wa'ada - a'daaaaaa' ali imron 103
    // bug -> wa'ada - wa-'aadin wa-tsamuud attaubah 70
    // bug -> wa'ada - ya'uudu almujadilah 8
    // bug -> wajaha - yunjiih al ma'arij 14
    // --------------------------------------------
    val misal = { f: String, a: String, l: String ->
        """$pre([$f$mudhori]$tasydid?[$primaryHarakat$madd]?$alif?(?:$ta[$fathah$kasrah$dommah])?$a$tasydid?[$primaryHarakat$madd]?[$waw$ya$alif]?$sukun?$l)(?!$tasydid)$end"""
    }

    // --------------------------------------------
    // atau dibuang ain nya seperti qul dan kun
    // --------------------------------------------
    // بَايعَ <- sudah bisa
    // bug -> اتبَاعَ - بَاع ,قال - قتال
    // bug -> maata (mati) - mu'tuun zakat : annisa 162
    // bug -> maata (mati) - mi'ataini (200): al-anfal 65 66
    // bug -> maata (mati) - ma'tiyya (datang): maryam 61
    // bug -> khaafa - tukhoofit: alisro 110
    // bug -> shaana - yuushiina: annisa 12; washshaina: annnisa 131
    // bug -> saara - sayaraa: attaubah 94; yasiir: yusuf 65
    // bug -> baqaa - biquwwah: albaqarah 63
    // bug -> laama, laana iseh ngebug
    // bug -> qaala - qitaal albaqarah 216; qul di al-falaq, al ikhlas kok ra muncul yo
    // bug -> kaana  - kun di yasin terakhir ga muncul
    // --------------------------------------------
    val ajwaf = { f: String, a: String, l: String ->
        "$pre(?:" +
            "($f$tasydid?[$primaryHarakat$madd]?$alif?$sukunSm?[$madd6$madd6lg]?(?:$ta[$primaryHarakat])?" +
            "[$a$ya$waw$alifMaqsuroh$alifMaqsuroh2$hmzs]$tasydid?[$primaryHarakat$madd]?[$madd6$madd6lg]?[$waw$ya$alif]?$sukun?$l)" +
            "|($f[$allHarakats]$l[\\s$sukun])" +
        ")$end"
    }

    // --------------------------------------------
    // ini ain fiilnya dibikin ain/kasrotain (ada dua capturing group; yg pertama normal, yg kedua ainnya diganti harakatain tp sblmnya harus ada alif)
    // trus paling akhir ga boleh ada ain[fthtain,ksrtain,dmmtain]
    // --------------------------------------------
    // bug -> romaa(romaya) - akrimiy
    // bug -> roma[$ya$alfMaqsuroh] kok bedo hasile
    // bug -> hana[$ya$alfMaqsuroh] yo bedo
    // bug -> radhiya - mardhaa sakit
    // bug -> riba - malah rabbii okeh bgt
    // bug -> pokoke antara ya karo alifMaqsuroh hasile iseh bedo
    // bug -> ra'aa (melihat). pokoke ajwaf ono sing mahmuzFa. tp ki wes ono pengecekan elek"an (lg mahmuzFa 'ain)
    // --------------------------------------------
    val naqis = { f: String, a: String, l: String ->
        val aFiil = if (a == hmz1 || a == hmz3) hmzs else a

        "$pre(?:" +
            "($f$tasydid?[$primaryHarakat$madd]?$alif?(?:$ta[$fathah$kasrah$dommah])?" +
            "[$aFiil]$tasydid?[$primaryHarakat$madd]?[$waw$ya$alif$alifMaqsuroh$alifMaqsuroh2]?$sukun?[$madd6$madd6lg]?$tatweel?" +
            "[$l$hmzs$ya$alifMaqsuroh$alifMaqsuroh2$waw])" +
            "|($f$fathah$alif$a$kasratan)" +
        ")(?![^ ]?$a[$fathatan$kasratan$dommatan])$end"
    }

    val lafif = { f: String, a: String, l: String ->
        "$pre(" +
            "$f$tasydid?[$primaryHarakat$madd]?$alif?(?:$ta[$fathah$kasrah$dommah])?" +
            "$a$tasydid?[$primaryHarakat$madd]?[$waw$ya$alif]?$sukun?$l" +
        ")$end"
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