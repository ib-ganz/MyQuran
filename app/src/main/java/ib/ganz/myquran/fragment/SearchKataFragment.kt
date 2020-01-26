package ib.ganz.myquran.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ib.ganz.myquran.R
import ib.ganz.myquran.arabword.WordProcessor
import ib.ganz.myquran.database.AyatData
import ib.ganz.myquran.kotlinstuff.*
import kotlinx.android.synthetic.main.fragment_search_kata.*
import kotlinx.android.synthetic.main.item_search_result.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class SearchKataFragment : BaseFragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val pd: ProgressDialog by lazy { ProgressDialog(activity!!) }
    private val lAyat = mutableListOf<AyatData>()
    private val searchAdapter: RecyclerView.Adapter<*> by lazy {
        adapter(activity!!, lAyat, R.layout.item_search_result) {
            lAyat[adapterPosition].run {
                tAyat.text = spannableText
                tLokasi.text = "$namaSurat $idSurat:$nomorAyat"
            }
        }
    }

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
        bSearch.click {
            pd.show()
            WordProcessor.process(activity!!, this, eInput.str()) {
                onSuccess { list, count ->
                    pd.dismiss()
                    tCount.text = "Count: $count"

                    lAyat.replaceWith(list)
                    searchAdapter.notifyDataSetChanged()
                }
                onError {
                    pd.dismiss()
                    Toast.makeText(activity!!, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}