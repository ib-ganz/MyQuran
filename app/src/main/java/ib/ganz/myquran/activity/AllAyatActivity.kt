package ib.ganz.myquran.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import ib.ganz.myquran.R
import ib.ganz.myquran.database.AyatData
import ib.ganz.myquran.kotlinstuff.adapter
import ib.ganz.myquran.kotlinstuff.extfun.d
import ib.ganz.myquran.kotlinstuff.extfun.toast
import ib.ganz.myquran.kotlinstuff.loadAsset
import ib.ganz.myquran.kotlinstuff.toggleInvisibility
import ib.ganz.myquran.manager.PrefManager
import kotlinx.android.synthetic.main.activity_all_ayat.*
import kotlinx.android.synthetic.main.item_tafsir_ayat.tNama
import kotlinx.android.synthetic.main.item_tafsir_ayat_all.*

class AllAyatActivity : BaseActivity() {

    companion object {
        fun go(c: Context, idSurat: String) = c.startActivity(Intent(c, AllAyatActivity::class.java).apply {
            putExtra("idSurat", idSurat)
        })
    }

    private val idSurat by lazy { intent.getStringExtra("idSurat") }
    private val lAyat by lazy { AyatData.getByIdSurat(a, idSurat) }
    private val ayatAdapter by lazy { buildAyatAdapter() }
    private val layManager by lazy { LinearLayoutManager(a) }

    private var isPlayingSelected = true
    private lateinit var currentAyat: AyatData
    private var currentAyatIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_ayat)

        bBack.setOnClickListener {
            vibrate()
            finish()
        }
        bPlayPause.setOnClickListener {
            if (isPlayingSelected) {
                isPlayingSelected = false
                stopTts()
            }
            else {
                isPlayingSelected = true
                speak { currentAyat.tafsir.toSpeech() }
            }

            vibrate()
            bPlayPause.setImageResource(if (isPlayingSelected) R.drawable.ic_pause_black_24dp else R.drawable.ic_play_arrow_black_24dp)
            ayatAdapter.notifyItemChanged(currentAyatIndex)
        }

        val upDown = { up: Boolean ->
            if (up) currentAyatIndex -= if (currentAyatIndex == 0) 0 else 1
            else currentAyatIndex += if (currentAyatIndex == lAyat.size - 1) 0 else 1
            currentAyat = lAyat[currentAyatIndex]

            isPlayingSelected = false
            stopTts()
            isPlayingSelected = true

            vibrate()
            playToCurrent()
            bPlayPause.setImageResource(R.drawable.ic_pause_black_24dp)
        }
        bUp.setOnClickListener { upDown(true) }
        bDown.setOnClickListener { upDown(false) }
        bPlayPause.setImageResource(if (PrefManager.getSuara()) R.drawable.ic_pause_black_24dp else R.drawable.ic_play_arrow_black_24dp)

        currentAyat = lAyat[currentAyatIndex]
        iSurat.loadAsset { "zz_${idSurat}.png" }
        tSurat.text = "${currentAyat.namaSurat}: ${currentAyat.nomorAyat}"
        rvAyat.run {
            layoutManager = layManager
            adapter = ayatAdapter
            addItemDecoration(DividerItemDecoration(a, RecyclerView.VERTICAL))
        }
    }

    override fun onTtsReady() {
        speak(currentAyat.idAyat) { currentAyat.tafsir.toSpeech() }
    }

    override val isPlaying: () -> Boolean
        get() = { isPlayingSelected }

    override val onTtsDone: () -> Unit
        get() = {
            if (++currentAyatIndex >= lAyat.size) {
                ayatAdapter.notifyDataSetChanged()
            }
            else {
                currentAyat = lAyat[currentAyatIndex]
                playToCurrent()
            }
        }

    // to call this, make sure you have set the currentAyat & currentAyatIndex
    private fun playToCurrent() {
        ayatAdapter.notifyDataSetChanged()
        layManager.scrollToPositionWithOffset(currentAyatIndex, 10)
        tSurat.text = "${currentAyat.namaSurat}: ${currentAyat.nomorAyat}"
        speak(currentAyat.idAyat) { currentAyat.tafsir.toSpeech() }
    }

    private fun String.toSpeech(): String {
        return "${currentAyat.namaSurat} ayat ${currentAyat.nomorAyat}....$this"
    }

    private fun buildAyatAdapter() = adapter(a, lAyat, R.layout.item_tafsir_ayat_all) {
        val s = lAyat[adapterPosition]
        tNama.text = "${s.namaSurat}: ${s.nomorAyat}"
        tTafsir.text = s.tafsir
        tTafsir.textSize = PrefManager.getTafsirSize()
        iIndicator.setImageResource(if (isPlayingSelected) R.drawable.ic_play_arrow_black_24dp else R.drawable.ic_pause_black_24dp)
        iIndicator.toggleInvisibility(currentAyat.idAyat == s.idAyat)
    }
}
