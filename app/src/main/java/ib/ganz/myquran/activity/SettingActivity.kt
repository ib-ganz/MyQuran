package ib.ganz.myquran.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import ib.ganz.myquran.R
import ib.ganz.myquran.database.AyatData
import ib.ganz.myquran.helper.Mp3Player
import ib.ganz.myquran.kotlinstuff.beVisible
import ib.ganz.myquran.kotlinstuff.toArabicNumber
import ib.ganz.myquran.manager.PrefManager
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.coroutines.launch


class SettingActivity : BaseActivity() {

    companion object {
        const val PENGATURAN_TOMBOL = 0
        const val SUARA = 1
        const val QARI = 2
        const val QURAN = 3
        const val TERJEMAH = 4
        const val TAFSIR = 5

        fun go(c: Context, type: Int) = c.startActivity(Intent(c, SettingActivity::class.java).apply {
            putExtra("type", type)
        })
    }

    private val type by lazy { intent.getIntExtra("type", 0) }
    private val rbTombols by lazy { arrayOf(rb1, rb2, rb3, rb4, rb5, rb6, rb7) }
    private val rbQaris by lazy { arrayOf(rbAbdulBaset, rbAlafasy, rbMinshawi, rbRifai, rbShatri, rbShuraym, rbSudais) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        when (type) {
            PENGATURAN_TOMBOL -> initPengaturanTombol()
            SUARA -> initSuara()
            QARI -> initQari()
            QURAN -> initQuran()
            TERJEMAH -> initTerjemah()
            TAFSIR -> initTafsir()
        }

        bBack.onClick("kembali") { finish() }
    }

    private fun initTafsir() {
        tWhat.text = "Pengaturan > Teks > Tafsir"
        lTafsir.beVisible()
        val min = 10

        tTafsirNow.text = "${PrefManager.getTafsirSize()}".replace(".0", "")
        tTafsir.textSize = PrefManager.getTafsirSize()
        sbTafsir.progress = PrefManager.getTafsirSize().toInt() - min
        sbTafsir.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var progressChanged: Int = min
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                progressChanged = min + progress
                PrefManager.setTafsirSize(progressChanged.toFloat())
                tTafsir.textSize = progressChanged.toFloat()
                tTafsirNow.text = "$progressChanged"
            }
            override fun onStartTrackingTouch(p0: SeekBar?) { }
            override fun onStopTrackingTouch(p0: SeekBar?) { }
        })
    }

    private fun initTerjemah() {
        tWhat.text = "Pengaturan > Teks > Terjemah"
        lTerjemah.beVisible()
        val min = 10

        tTerjemahNow.text = "${PrefManager.getTerjemahSize()}".replace(".0", "")
        tTerjemah.textSize = PrefManager.getTerjemahSize()
        sbTerjemah.progress = PrefManager.getTerjemahSize().toInt() - min
        sbTerjemah.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var progressChanged: Int = min
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                progressChanged = min + progress
                PrefManager.setTerjemahSize(progressChanged.toFloat())
                tTerjemah.textSize = progressChanged.toFloat()
                tTerjemahNow.text = "$progressChanged"
            }
            override fun onStartTrackingTouch(p0: SeekBar?) { }
            override fun onStopTrackingTouch(p0: SeekBar?) { }
        })
    }

    private fun initQuran() {
        tWhat.text = "Pengaturan > Teks > Al-Qur'an"
        lQuran.beVisible()
        val min = 12

        tQuranNow.text = "${PrefManager.getQuranSize()}".replace(".0", "")
        tQuran.textSize = PrefManager.getQuranSize()
        tQuran.text = "بِسْمِ ٱللَّهِ ٱلرَّحْمٰنِ ٱلرَّحِيمِ "
        tQuran.text = "${tQuran.text}    \uFD3F${1.toArabicNumber()}\uFD3E    "
        sbQuran.progress = PrefManager.getQuranSize().toInt() - min
        sbQuran.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var progressChanged: Int = min
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                progressChanged = min + progress
                PrefManager.setQuranSize(progressChanged.toFloat())
                tQuran.textSize = progressChanged.toFloat()
                tQuranNow.text = "$progressChanged"
            }
            override fun onStartTrackingTouch(p0: SeekBar?) { }
            override fun onStopTrackingTouch(p0: SeekBar?) { }
        })
    }

    private fun initQari() {
        tWhat.text = "Pengaturan > Aplikasi > Qari'"
        lQari.beVisible()
        try {
            rbQaris[PrefManager.getQariPos()].isChecked = true
            rgQari.setOnCheckedChangeListener { _, id ->
                PrefManager.setQariPos(rbQaris.indexOfFirst { it.id == id })
                speak { PrefManager.getQari().replace("AbdulBaset", "Abdul Baset").replace("/Murattal", "") }
            }
        }
        catch (e: Exception) {
            speak { e.message ?: "terjadi kesalahan" }
        }
    }

    private fun initSuara() {
        tWhat.text = "Pengaturan > Aplikasi > Suara"
        lSuara.beVisible()
        swSuara.isChecked = PrefManager.getSuara()
        swSuara.setOnCheckedChangeListener { _, b ->
            PrefManager.setSuara(b)
        }
    }

    private fun initPengaturanTombol() {
        tWhat.text = "Pengaturan > Aplikasi > Tombol"
        lTombol.beVisible()

        rbTombols[PrefManager.getButtonMode()].isChecked = true
        tCurrentMode.text = rbTombols[PrefManager.getButtonMode()].text

        rgTombol.setOnCheckedChangeListener { _, id ->
            PrefManager.setButtonMode(rbTombols.indexOfFirst { it.id == id })
            tCurrentMode.text = rbTombols[PrefManager.getButtonMode()].text
            speak { tCurrentMode.text.toString().replace(" ->", "kemudian").replace("->", "langsung") }
        }
        bTes.onClick("putar bismillah") {
            launch { Mp3Player.play(AyatData.getBismillah(a)) }
        }
    }
}
