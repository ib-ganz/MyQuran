package ib.ganz.myquran.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import ib.ganz.myquran.R
import ib.ganz.myquran.database.AyatData
import ib.ganz.myquran.database.SuratData
import ib.ganz.myquran.kotlinstuff.*
import kotlinx.android.synthetic.main.activity_search_sound.*
import kotlinx.android.synthetic.main.item_tafsir_surat.*
import kotlinx.android.synthetic.main.item_tafsir_surat.tNama

class SearchSoundActivity : BaseActivity() {

    companion object {
        fun goWithResult(c: Activity) = c.startActivityForResult(Intent(c, SearchSoundActivity::class.java), 666)
    }

    private val lSurat by lazy { SuratData.getAll(a) }
    private val suratAdapter by lazy { buildSuratAdapter() }
    private var idSurat = ""
    private lateinit var lAyat: MutableList<AyatData>
    private lateinit var ayatAdapter: RecyclerView.Adapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_sound)

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

    private fun onSuratSelected(s: SuratData) {
        idSurat = s.idSurat
        speak { "silahkan pilih ayat" }
        rvSurat.beGone()
        rvAyat.beVisible()
        tRes.text = s.nama

        lAyat = AyatData.getByIdSurat(a, idSurat)
        ayatAdapter = buildAyatAdapter()
        rvAyat.run {
            layoutManager = LinearLayoutManager(a)
            adapter = ayatAdapter
            addItemDecoration(DividerItemDecoration(a, RecyclerView.VERTICAL))
        }
    }

    private fun buildSuratAdapter() = adapter(a, lSurat, R.layout.item_tafsir_surat) {
        val s = lSurat[adapterPosition]
        tNama.text = s.nama
        tJumlah.text = "Jumlah ayat: ${s.jumlahAyat}"
        iGambar.loadAsset { "zz_${s.idSurat}.png" }

        containerView.onClick("Surat ${s.nama}", adapterPosition) {
            onSuratSelected(s)
        }
    }

    private fun buildAyatAdapter() = adapter(a, lAyat, R.layout.item_tafsir_ayat) {
        val s = lAyat[adapterPosition]
        tNama.text = "${s.namaSurat}: ${s.nomorAyat}"

        containerView.onClick("${s.namaSurat} ayat ${s.nomorAyat}", adapterPosition) {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra("res", s.toJsonObject())
            })
            finish()
        }
    }
}
