package ib.ganz.myquran.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import ib.ganz.myquran.R
import ib.ganz.myquran.database.SuratData
import ib.ganz.myquran.kotlinstuff.adapter
import ib.ganz.myquran.kotlinstuff.loadAsset
import kotlinx.android.synthetic.main.activity_list_surat.*
import kotlinx.android.synthetic.main.item_tafsir_surat.*

class ListSuratActivity : BaseActivity() {

    companion object {
        fun go(c: Context) = c.startActivity(Intent(c, ListSuratActivity::class.java))
    }

    private val lSurat by lazy { SuratData.getAll(a) }
    private val suratAdapter by lazy { buildSuratAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_surat)

        bBack.onClick("kembali") {
            finish()
        }

        rvSurat.run {
            layoutManager = LinearLayoutManager(a)
            adapter = suratAdapter
            addItemDecoration(DividerItemDecoration(a, RecyclerView.VERTICAL))
        }
    }

    override fun onTtsReady() {
        speak { "Silahkan pilih surat" }
    }

    private fun buildSuratAdapter() = adapter(a, lSurat, R.layout.item_tafsir_surat) {
        val s = lSurat[adapterPosition]
        tNama.text = s.nama
        tJumlah.text = "Jumlah ayat: ${s.jumlahAyat}"
        iGambar.loadAsset { "zz_${s.idSurat}.png" }

        containerView.onClick("Surat ${s.nama}", adapterPosition) {
            ListAyatActivity.go(a, s.idSurat)
        }
    }
}
