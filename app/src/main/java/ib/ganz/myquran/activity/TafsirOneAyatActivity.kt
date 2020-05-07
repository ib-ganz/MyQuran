package ib.ganz.myquran.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import ib.ganz.myquran.R
import ib.ganz.myquran.database.AyatData
import ib.ganz.myquran.database.DetailAyatData
import ib.ganz.myquran.database.SuratData
import ib.ganz.myquran.helper.Mp3Player
import ib.ganz.myquran.kotlinstuff.loadAsset
import ib.ganz.myquran.kotlinstuff.str
import ib.ganz.myquran.kotlinstuff.toArabicNumber
import ib.ganz.myquran.manager.PrefManager
import kotlinx.android.synthetic.main.activity_tafsir_one_ayat.*
import kotlinx.coroutines.launch

class TafsirOneAyatActivity : BaseActivity() {

    companion object {
        fun go(c: Context, idAyat: String) = c.startActivity(Intent(c, TafsirOneAyatActivity::class.java).apply {
            putExtra("idAyat", idAyat)
        })
    }

    private val idAyat by lazy { intent.getStringExtra("idAyat") }
    private val ayatData: AyatData by lazy { AyatData.getByIdAyat(a, idAyat) }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tafsir_one_ayat)

        bBack.onClick("kembali") {
            finish()
        }

        val suratData = SuratData.getByIdAyat(this, idAyat)
        iSurat.loadAsset { "zz_${suratData.idSurat}.png" }
        tSurat.text = "${ayatData.namaSurat}: ${ayatData.nomorAyat}"

        tAyat.text = "${ayatData.text}    \uFD3F${ayatData.nomorAyat.toArabicNumber()}\uFD3E    "
        tTafsir.text = ayatData.tafsir
        tTerjemah.text = DetailAyatData.getDetail(a, ayatData.idAyat).first

        tAyat.textSize = PrefManager.getQuranSize()
        tTafsir.textSize = PrefManager.getTafsirSize()
        tTerjemah.textSize = PrefManager.getTerjemahSize()

        val speechAyat = "surat ${ayatData.namaSurat.replace("`", "")} ayat ${ayatData.nomorAyat}"
        tAyat.onClick(speechAyat, { Mp3Player.stop() }) {
            launch { Mp3Player.play(ayatData) { speak { it } } }
        }
        val speechTerjemah = "terjemah surat ${ayatData.namaSurat.replace("`", "")} ayat ${ayatData.nomorAyat}"
        tTerjemah.onClick(speechTerjemah, { Mp3Player.stop() }) {
            speak { tTerjemah.str() }
        }
        val speechTafsir = "tafsir surat ${ayatData.namaSurat.replace("`", "")} ayat ${ayatData.nomorAyat}"
        tTafsir.onClick(speechTafsir, { Mp3Player.stop() }) {
            speak { ayatData.tafsir }
        }
    }

    override fun onBackPressed() {
        Mp3Player.stop()
        super.onBackPressed()
    }
}
