package ib.ganz.myquran.activity

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import ib.ganz.emaklon.helper.ExitManager
import ib.ganz.myquran.R
import ib.ganz.myquran.customclass.BackgroundManager
import ib.ganz.myquran.database.AyatData
import ib.ganz.myquran.kotlinstuff.adapter
import ib.ganz.myquran.kotlinstuff.click
import ib.ganz.myquran.kotlinstuff.toArabicNumber
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_ayat.*
import kotlinx.android.synthetic.main.view_last_read.*

class MainActivity : BaseActivity() {

    private val bottomSheet: BottomSheetBehavior<LinearLayout> by lazy { BottomSheetBehavior.from(findViewById<LinearLayout>(R.id.bottomSheet)) }
    private val lAyat: MutableList<AyatData> by lazy { AyatData.getFatihah(a) }
    private val adapter: RecyclerView.Adapter<*> by lazy { initAdapter() }

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

        bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
                bSlide.alpha = 1 - p1
            }
            override fun onStateChanged(p0: View, p1: Int) { }
        })

        rvAyat.adapter = adapter
    }

    private fun initAdapter() = adapter(a, lAyat, R.layout.item_ayat) {
        val a = lAyat[adapterPosition]
        tAyat.text = "${a.text}    \uFD3F${a.nomorAyat.toArabicNumber()}\uFD3E    "
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
