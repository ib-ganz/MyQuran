package ib.ganz.myquran.helper

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import ib.ganz.myquran.R
import ib.ganz.myquran.activity.BaseActivity
import ib.ganz.myquran.database.AyatData
import ib.ganz.myquran.database.QuranData
import ib.ganz.myquran.kotlinstuff.extfun.toast
import ib.ganz.myquran.kotlinstuff.loadAsset
import ib.ganz.myquran.kotlinstuff.then
import ib.ganz.myquran.manager.PrefManager
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_last_read.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

class LastReadHelper(
    private val a: BaseActivity,
    private val cc: CoroutineScope,
    override val containerView: View?
) : LayoutContainer {

    private val lAyat: MutableList<AyatData> by lazy { AyatData.getAll(a) }
    private val quranAdapter: RecyclerView.Adapter<*> by lazy { QuranAdapter(a, lQuran) { qp -> quranPlayback = qp } }
    private val lQuran: MutableList<QuranData> by lazy { QuranData.fromListAyat(lAyat) }
    private var lastReadPosition = PrefManager.getLastReadPosition()

    private val lm by lazy { rvAyat.layoutManager as LinearLayoutManager }
    private var isPlaying = false
    private lateinit var quranPlayback : QuranPlayback
    private var hasBeenStopped = false

    fun help() {
        if (lastReadPosition < lQuran.size) {
            setHeader()
        }
        download()
        initViews()
    }

    fun stop() {
        hasBeenStopped = true
        isPlaying = false
        Mp3Player.onDone(null)
        Mp3Player.stop()
        bPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp)
    }

    fun onResume() {
        Mp3Player.onDone {
            quranPlayback.onDoneSounding(lQuran[lastReadPosition])
            play(1)
        }
        quranAdapter.notifyDataSetChanged()
    }

    fun onPause() {
        Mp3Player.onDone(null)
        Mp3Player.stop()
    }

    fun onSoundSelected(a: AyatData) {
        val q = lQuran.find { it.idAyat == a.idAyat && !it.isHeader }
        val pos = lQuran.indexOf(q)

        onResume()
        PrefManager.setLastReadPosition(pos)
        lastReadPosition = PrefManager.getLastReadPosition()
        setHeader()
        isPlaying = true
        play(0)
    }

    private fun setHeader() {
        val lastAyat: QuranData = lQuran[lastReadPosition]
        iSurat.loadAsset { "zz_${lastAyat.nomorSurat}.png" }
        tSurat.text = if (lastAyat.isHeader) lastAyat.namaSurat else "${lastAyat.namaSurat}: ${lastAyat.nomorAyat}"
        lm.scrollToPositionWithOffset(lastReadPosition, 0)
    }

    private fun setLastReadPosition(up: Boolean) : Boolean {
        if (up && lastReadPosition > 0) {
            PrefManager.setLastReadPosition(PrefManager.getLastReadPosition() - 1)
            lastReadPosition = PrefManager.getLastReadPosition()
            setHeader()
            return true
        }
        else if (!up && lastReadPosition < lQuran.size - 1) {
            PrefManager.setLastReadPosition(PrefManager.getLastReadPosition() + 1)
            lastReadPosition = PrefManager.getLastReadPosition()
            setHeader()
            return true
        }
        return false
    }

    private fun download() = cc.launch {
        val left = lastReadPosition - 3
        val right = lastReadPosition + 5
        (left..right).forEach {
            try {
                Mp3Downloader.download(lQuran[it].toAyatData())
            }
            catch (e: Exception) { }
        }
    }

    private fun play(addIndex: Int) {
        download()
        bPlayPause.setImageResource(R.drawable.ic_pause_black_24dp)

        val p: () -> Unit = {
            val q = lQuran[lastReadPosition]
            quranPlayback.onClickPlay(q)

            var ayat = q.toAyatData()
            if (q.isHeader) {
                ayat = AyatData(
                    idAyat = "1",
                    idSurat = "1",
                    nomorAyat = "1",
                    text = "",
                    mentahan = "",
                    namaSurat = "",
                    jumlahAyat = "7",
                    tafsir = ""
                )
            }

            cc.launch {
                Mp3Player.play(ayat, { quranPlayback.onStartSounding(q) }) {
                    a.toast { it }
                    a.speak { "Periksa koneksi anda" }
                    quranPlayback.onErrorSounding(q)
                }
            }
        }

        when (addIndex) {
            1 -> setLastReadPosition(false).then(p)
            -1 -> setLastReadPosition(true).then(p)
            else -> p()
        }
    }

    private fun initViews() {
        bPlayPause.setOnClickListener {
            a.vibrate()
            if (isPlaying) {
                isPlaying = false
                Mp3Player.stop()
                bPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                quranPlayback.onPauseSounding(lQuran[lastReadPosition])
            }
            else {
                if (hasBeenStopped) {
                    onResume()
                }
                isPlaying = true
                play(0)
            }
        }
        bUp.setOnClickListener {
            a.vibrate()
            Mp3Player.stop()
            play(-1)
        }
        bDown.setOnClickListener {
            a.vibrate()
            Mp3Player.stop()
            play(1)
        }

        rvAyat.adapter = quranAdapter
        if (lastReadPosition < lQuran.size) {
            lm.scrollToPosition(lastReadPosition)
        }
    }

    private fun QuranData.toAyatData() = AyatData.getByIdAyat(a, this.idAyat)
}