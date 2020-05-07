package ib.ganz.myquran.helper

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ib.ganz.myquran.R
import ib.ganz.myquran.database.DetailAyatData
import ib.ganz.myquran.database.QuranData
import ib.ganz.myquran.kotlinstuff.*
import ib.ganz.myquran.manager.PrefManager
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_ayat.*
import kotlinx.android.synthetic.main.item_header.*

class QuranAdapter(
    private val c: Context,
    private val lQuran: MutableList<QuranData>,
    qp: (QuranPlayback) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ayatType = 0
    private val headerType = 1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return if (p1 == ayatType) AyatVh(LayoutInflater.from(c).inflate(R.layout.item_ayat, p0, false))
        else HeaderVh(LayoutInflater.from(c).inflate(R.layout.item_header, p0, false))
    }

    override fun getItemViewType(position: Int): Int {
        return if (lQuran[position].isHeader) headerType else ayatType
    }

    override fun getItemCount(): Int {
        return lQuran.size
    }

    override fun onBindViewHolder(h: RecyclerView.ViewHolder, p1: Int) {
        val q = lQuran[p1]
        if (h is AyatVh) {
            h.tAyat.textSize = PrefManager.getQuranSize()
            h.tAyat.text = "${q.text}    \uFD3F${q.nomorAyat.toArabicNumber()}\uFD3E    "
            h.tAyat.click { showDetail(q) }

            h.pbAyat.toggleInvisibility(q.state == QuranData.LOADING)
            h.iAyatIndincator.toggleInvisibility(q.state == QuranData.PLAYING || q.state == QuranData.PAUSED)
            h.iAyatIndincator.setImageResource(if (q.state == QuranData.PAUSED) R.drawable.ic_pause_black_24dp else R.drawable.ic_play_arrow_black_24dp)
        }
        else if (h is HeaderVh) {
            h.iSurat.loadAsset { "zz_${q.nomorSurat}.png" }
            h.tSurat.text = "${q.namaSurat} (${q.jumlahAyat} Ayat)"
        }
    }

    private fun showDetail(q: QuranData) {
        val v = LayoutInflater.from(c).inflate(R.layout.dialog_detail_ayat, null).apply {
            val det = DetailAyatData.getDetail(c, q.idAyat)
            val ter = findViewById<TextView>(R.id.tTerjemah)
            val taf = findViewById<TextView>(R.id.tTafsir)

            findViewById<TextView>(R.id.tHeader).text = "QS ${q.namaSurat} ${q.nomorSurat}:${q.nomorAyat}"
            ter.textSize = PrefManager.getTerjemahSize()
            taf.textSize = PrefManager.getTafsirSize()
            ter.text = det.first
            taf.text = det.second
        }
        AlertDialog.Builder(c)
            .setView(v)
            .setPositiveButton("Ok", null)
            .show()
    }

    init {
        qp(object : QuranPlayback {
            override fun onClickPlay(q: QuranData) {
                val newLq = lQuran.map {
                    it.apply {
                        val state = if (it.idAyat == q.idAyat) QuranData.LOADING else QuranData.NOTHING
                        it.state = state
                    }
                }
                lQuran.replaceWith(newLq.toMutableList())

                notifyDataSetChanged()
            }
            override fun onStartSounding(q: QuranData) {
                val newLq = lQuran.map {
                    it.apply {
                        val state = if (it.idAyat == q.idAyat) QuranData.PLAYING else QuranData.NOTHING
                        it.state = state
                    }
                }
                lQuran.replaceWith(newLq.toMutableList())

                notifyDataSetChanged()
            }
            override fun onPauseSounding(q: QuranData) {
                val newLq = lQuran.map {
                    it.apply {
                        val state = if (it.idAyat == q.idAyat) QuranData.PAUSED else QuranData.NOTHING
                        it.state = state
                    }
                }
                lQuran.replaceWith(newLq.toMutableList())

                notifyDataSetChanged()
            }
            override fun onDoneSounding(q: QuranData) {
                val newLq = lQuran.map {
                    it.apply {
                        it.state = QuranData.NOTHING
                    }
                }
                lQuran.replaceWith(newLq.toMutableList())

                notifyDataSetChanged()
            }
            override fun onErrorSounding(q: QuranData) {
                val newLq = lQuran.map {
                    it.apply {
                        it.state = QuranData.NOTHING
                    }
                }
                lQuran.replaceWith(newLq.toMutableList())

                notifyDataSetChanged()
            }
        })
    }
}

class AyatVh(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

class HeaderVh(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

interface QuranPlayback {
    fun onClickPlay(q: QuranData)
    fun onStartSounding(q: QuranData)
    fun onPauseSounding(q: QuranData)
    fun onDoneSounding(q: QuranData)
    fun onErrorSounding(q: QuranData)
}