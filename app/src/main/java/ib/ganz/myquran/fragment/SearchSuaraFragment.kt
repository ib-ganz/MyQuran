package ib.ganz.myquran.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ib.ganz.myquran.R
import ib.ganz.myquran.database.AyatData
import ib.ganz.myquran.kotlinstuff.adapter
import ib.ganz.myquran.kotlinstuff.click
import ib.ganz.myquran.kotlinstuff.replaceWith
import ib.ganz.myquran.kotlinstuff.toArabicNumber
import kotlinx.android.synthetic.main.fragment_search_suara.*
import kotlinx.android.synthetic.main.item_search_result.*

class SearchSuaraFragment : Fragment() {

    private val lAyat = mutableListOf<AyatData>()
    private val searchAdapter: RecyclerView.Adapter<*> by lazy {
        adapter(activity!!, lAyat, R.layout.item_search_result) {
            lAyat[adapterPosition].run {
                tAyat.text = "$text    \uFD3F${nomorAyat.toArabicNumber()}\uFD3E    "
                tLokasi.text = "$namaSurat:$nomorAyat"
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_suara, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 900 && resultCode == Activity.RESULT_OK && data != null) {
            val res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            lAyat.replaceWith(AyatData.getFromVoice(activity!!, res))
            searchAdapter.notifyDataSetChanged()
        }
    }

    private fun initViews() {
        bStart.click { rekam() }
        rvSearch.adapter = searchAdapter
    }

    private fun rekam() {
        val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Silahkan baca ayat")
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-AE")
        startActivityForResult(i, 900)
    }
}