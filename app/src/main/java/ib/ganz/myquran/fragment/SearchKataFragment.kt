package ib.ganz.myquran.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ib.ganz.myquran.R
import ib.ganz.myquran.activity.PencarianActivity
import ib.ganz.myquran.arabword.WordProcessor
import ib.ganz.myquran.database.AyatData
import ib.ganz.myquran.helper.Mp3Player
import ib.ganz.myquran.kotlinstuff.*
import ib.ganz.myquran.manager.PrefManager
import kotlinx.android.synthetic.main.fragment_search_kata.*
import kotlinx.android.synthetic.main.item_search_result.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SearchKataFragment : BaseFragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val pd: ProgressDialog by lazy { ProgressDialog(activity!!) }
    private val lAyat = mutableListOf<AyatData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_kata, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvResult.adapter = searchAdapter

        val f = {
            Mp3Player.stop()
            (activity as PencarianActivity).stopSpeaking()
        }
        bSearch.onClick("cari kata", f) {
            tlInput.error = null
            if (eInput.isEmpty()) {
                tlInput.error = "masukkan akar kata arab dahulu"
                speak { "masukkan akar kata arab dahulu" }
            }
            else {
                performSearch()
            }
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    private val performSearch = {
        pd.show()
        WordProcessor.process(activity!!, this, eInput.str()) {
            onSuccess { list, count ->
                pd.dismiss()
                val wRes = if (count > 0) "Ditemukan sebanyak $count ayat" else "Ayat tidak ditemukan"
                val key = "dengan kata kunci: ${eInput.str()}"
                tRes.text = wRes
                tKey.text = key
                speak { "$wRes $key" }

                lAyat.replaceWith(list)
                searchAdapter.notifyDataSetChanged()
            }
            onError {
                pd.dismiss()
                Toast.makeText(activity!!, it, Toast.LENGTH_SHORT).show()
                speak { it }
            }
        }
    }

    private val searchAdapter: RecyclerView.Adapter<*> by lazy {
        adapter(activity!!, lAyat, R.layout.item_search_result) {
            val d = lAyat[adapterPosition]
            tAyat.text = d.spannableText
            tLokasi.text = "${d.namaSurat}: ${d.nomorAyat}"
            tTafsir.text = d.tafsir

            tAyat.textSize = PrefManager.getQuranSize()
            tTafsir.textSize = PrefManager.getTafsirSize()

            val speechAyat = "surat ${d.namaSurat.replace("`", "")} ayat ${d.nomorAyat}"
            tAyat.onClick(speechAyat, adapterPosition, { Mp3Player.stop() }) {
                launch {
                    Mp3Player.play(d) { speak { it } }
                }
            }
            val speechTafsir = "tafsir surat ${d.namaSurat.replace("`", "")} ayat ${d.nomorAyat}"
            tTafsir.onClick(speechTafsir, adapterPosition, { Mp3Player.stop() }) {
                speak { d.tafsir }
            }
        }
    }
}