package ib.ganz.myquran.activity

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.RelativeLayout
import ib.ganz.emaklon.helper.ExitManager
import ib.ganz.myquran.R
import ib.ganz.myquran.customclass.BackgroundManager
import ib.ganz.myquran.database.AyatData
import ib.ganz.myquran.database.QuranData
import ib.ganz.myquran.helper.PrefManager
import ib.ganz.myquran.helper.QuranAdapter
import ib.ganz.myquran.kotlinstuff.click
import ib.ganz.myquran.kotlinstuff.getStatusBarHeight
import ib.ganz.myquran.kotlinstuff.loadAsset
import ib.ganz.myquran.kotlinstuff.then
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_last_read.*

class MainActivity : BaseActivity() {

    private val bottomSheet: BottomSheetBehavior<RelativeLayout> by lazy {
        BottomSheetBehavior.from(findViewById<RelativeLayout>(R.id.bottomSheet))
    }
    private val lAyat: MutableList<AyatData> by lazy { AyatData.getFatihah(a) }
    private val lQuran: MutableList<QuranData> by lazy { QuranData.fromListAyat(lAyat) }
    private val quranAdapter: RecyclerView.Adapter<*> by lazy { QuranAdapter(a, lQuran) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        bSetting.click {  }
        bCompass.click {  }
        jadwalView.run {
            compositeDisposable(compositeDisposable)
            onFinished { BackgroundManager.init(iBg, it) }
            start()
        }

        vStatusBar.run { layoutParams = (layoutParams as RelativeLayout.LayoutParams).also { it.setMargins(0, getStatusBarHeight(), 0, 0) } }
        bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(p0: View, p1: Int) { }
            override fun onSlide(p0: View, p1: Float) {
                bSlide.alpha = 1 - p1
                vToolbar.alpha = Math.max(0f, p1 - 0.75f) * 4
            }
        })

        initLastRead()
    }

    private fun initLastRead() {
        val lastReadPosition = PrefManager.getLastReadPosition()
        if (lastReadPosition < lQuran.size) {
            val a = lQuran[lastReadPosition]
            iSurat.loadAsset { "zz_${a.nomorSurat}.png" }
            tSurat.text = if (a.isHeader) a.namaSurat else "${a.namaSurat}: ${a.nomorAyat}"
        }

        rvAyat.adapter = quranAdapter
        val lm = (rvAyat.layoutManager as LinearLayoutManager).also {
            (lastReadPosition < lQuran.size).then { it.scrollToPosition(lastReadPosition) }
        }
        rvAyat.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val pos = lm.findFirstVisibleItemPosition()
                    if (PrefManager.isLastReadAutoSave()) {
                        PrefManager.setLastReadPosition(pos)
                    }

                    val a = lQuran[pos]
                    iSurat.loadAsset { "zz_${a.nomorSurat}.png" }
                    tSurat.text = if (a.isHeader) a.namaSurat else "${a.namaSurat}: ${a.nomorAyat}"
                }
            }
        })

        swSimpan.isChecked = PrefManager.isLastReadAutoSave()
        swSimpan.setOnCheckedChangeListener { _, b ->
            PrefManager.setIsLastReadAutoSave(b)
            toast(if (b) "Simpan otomatis aktif" else "Simpan otomatis tidak aktif")
        }
    }

    override fun onBackPressed() {
        if (bottomSheet.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        else {
            ExitManager.go(a)
        }
    }
}
