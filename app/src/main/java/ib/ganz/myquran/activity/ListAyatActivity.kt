package ib.ganz.myquran.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import ib.ganz.myquran.R
import ib.ganz.myquran.database.AyatData
import ib.ganz.myquran.database.SuratData
import ib.ganz.myquran.kotlinstuff.adapter
import ib.ganz.myquran.kotlinstuff.loadAsset
import kotlinx.android.synthetic.main.activity_list_ayat.*
import kotlinx.android.synthetic.main.item_tafsir_ayat.*

class ListAyatActivity : BaseActivity() {

    companion object {
        fun go(c: Context, idSurat: String) = c.startActivity(Intent(c, ListAyatActivity::class.java).apply {
            putExtra("idSurat", idSurat)
        })
    }

    private val idSurat by lazy { intent.getStringExtra("idSurat") }
    private val lAyat by lazy { AyatData.getByIdSurat(a, idSurat) }
    private val ayatAdapter by lazy { buildAyatAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_ayat)

        bBack.onClick("kembali") {
            finish()
        }
        bSemua.onClick("Semua ayat surat ${lAyat[0].namaSurat}") {
            AllAyatActivity.go(a, idSurat)
        }

        val suratData = SuratData.getByIdSurat(this, idSurat)
        iSurat.loadAsset { "zz_${suratData.idSurat}.png" }
        iSurat2.loadAsset { "zz_${suratData.idSurat}.png" }
        tSurat.text = suratData.nama
        tSurat2.text = suratData.nama

        rvAyat.run {
            layoutManager = LinearLayoutManager(a)
            adapter = ayatAdapter
            addItemDecoration(DividerItemDecoration(a, RecyclerView.VERTICAL))
        }
    }

    override fun onTtsReady() {
        speak { "Silahkan pilih ayat" }
    }

    private fun buildAyatAdapter() = adapter(a, lAyat, R.layout.item_tafsir_ayat) {
        val s = lAyat[adapterPosition]
        tNama.text = "${s.namaSurat}: ${s.nomorAyat}"

        containerView.onClick("${s.namaSurat} ayat ${s.nomorAyat}", adapterPosition) {
            TafsirOneAyatActivity.go(a, s.idAyat)
        }
    }
}
