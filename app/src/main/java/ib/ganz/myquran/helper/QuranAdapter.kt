package ib.ganz.myquran.helper

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ib.ganz.myquran.R
import ib.ganz.myquran.database.QuranData
import ib.ganz.myquran.kotlinstuff.loadAsset
import ib.ganz.myquran.kotlinstuff.toArabicNumber
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_ayat.*
import kotlinx.android.synthetic.main.item_header.*

class QuranAdapter(private val c: Context, private val lQuran: MutableList<QuranData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            h.tAyat.text = "${q.text}    \uFD3F${q.nomorAyat.toArabicNumber()}\uFD3E    "
        }
        else if (h is HeaderVh) {
            h.iSurat.loadAsset { "zz_${q.nomorSurat}.png" }
            h.tSurat.text = "${q.namaSurat} (${q.jumlahAyat} Ayat)"
        }
    }
}

class AyatVh(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

class HeaderVh(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer