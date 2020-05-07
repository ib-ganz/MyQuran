package ib.ganz.myquran.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ib.ganz.myquran.R
import ib.ganz.myquran.activity.PencarianActivity
import ib.ganz.myquran.database.AyatData
import ib.ganz.myquran.helper.Mp3Player
import ib.ganz.myquran.kotlinstuff.adapter
import ib.ganz.myquran.kotlinstuff.beGone
import ib.ganz.myquran.kotlinstuff.replaceWith
import ib.ganz.myquran.kotlinstuff.toArabicNumber
import ib.ganz.myquran.manager.PrefManager
import kotlinx.android.synthetic.main.fragment_search_suara.*
import kotlinx.android.synthetic.main.item_search_result.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


@SuppressLint("SetTextI18n")
class SearchSuaraFragment : BaseFragment() {

    private val lAyat = mutableListOf<AyatData>()
    private val searchAdapter: RecyclerView.Adapter<*> by lazy { buildSearchAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_suara, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val f = {
            Mp3Player.stop()
            (activity as PencarianActivity).stopSpeaking()
        }
        bStart.onClick("cari ayat", f) { rekam() }
        rvSearch.adapter = searchAdapter
        rvSearch.addItemDecoration(DividerItemDecoration(a, RecyclerView.VERTICAL))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 900 && resultCode == Activity.RESULT_OK && data != null) {
            val res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            lAyat.replaceWith(AyatData.getFromVoice(a, res))
            searchAdapter.notifyDataSetChanged()

            val wRes = if (lAyat.size > 0) "Ditemukan sebanyak ${lAyat.size} ayat" else "Ayat tidak ditemukan"
            speak { wRes }
            tRes.text = wRes
            tKey.text = "dengan kata kunci: ${res.joinToString(", ")}"
            tFirst.beGone()
        }
    }

    private fun buildSearchAdapter() = adapter(activity!!, lAyat, R.layout.item_search_result) {
        val la = lAyat[adapterPosition]
        tAyat.text = "${la.text}    \uFD3F${la.nomorAyat.toArabicNumber()}\uFD3E    "
        tLokasi.text = "${la.namaSurat}: ${la.nomorAyat}"
        tTafsir.text = la.tafsir

        tAyat.textSize = PrefManager.getQuranSize()
        tTafsir.textSize = PrefManager.getTafsirSize()

        val speechAyat = "surat ${la.namaSurat.replace("`", "")} ayat ${la.nomorAyat}"
        tAyat.onClick(speechAyat, adapterPosition, { Mp3Player.stop() }) {
            launch {
                Mp3Player.play(la) { speak { it } }
            }
        }
        val speechTafsir = "tafsir surat ${la.namaSurat.replace("`", "")} ayat ${la.nomorAyat}"
        tTafsir.onClick(speechTafsir, adapterPosition, { Mp3Player.stop() }) {
            speak { la.tafsir }
        }
    }

    private fun rekam() {
        val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Silahkan baca potongan ayat")
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-AE")
        startActivityForResult(i, 900)
    }
}