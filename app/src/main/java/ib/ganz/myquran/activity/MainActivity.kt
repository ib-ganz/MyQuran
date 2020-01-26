package ib.ganz.myquran.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.RelativeLayout
import ib.ganz.myquran.R
import ib.ganz.myquran.customclass.BackgroundManager
import ib.ganz.myquran.database.AyatData
import ib.ganz.myquran.database.QuranData
import ib.ganz.myquran.helper.ExitManager
import ib.ganz.myquran.manager.PrefManager
import ib.ganz.myquran.helper.QuranAdapter
import ib.ganz.myquran.kotlinstuff.extfun.reqStorageAndLocationPermissions
import ib.ganz.myquran.kotlinstuff.extfun.requestLocationPermission
import ib.ganz.myquran.kotlinstuff.extfun.requestStoragePermission
import ib.ganz.myquran.kotlinstuff.extfun.toast
import ib.ganz.myquran.kotlinstuff.getStatusBarHeight
import ib.ganz.myquran.kotlinstuff.loadAsset
import ib.ganz.myquran.kotlinstuff.then
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_last_read.*

class MainActivity : BaseActivity() {

    companion object {
        fun go(c: Activity) = c.run {
            startActivity(Intent(c, MainActivity::class.java))
            finish()
        }
    }

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
        reqStorageAndLocationPermissions()
    }

    override fun onTtsReady() {
        speak { "Selamat datang di aplikasi tafsir for blind" }
    }

    private fun initViews() {
        bSetting.onClick("pengaturan") { toast { "pengaturam" } }
        bCompass.onClick("kompas") { CompassActivity.go(a) }
        bSearch.onClick("pencarian ayat") { PencarianActivity.go(a) }
        bTafsir.onClick("kajian tafsir") { TafsirActivity.go(a) }
        jadwalView.run {
            scope { a }
            vibrator { { vibrate() } }
            speaker { { speak { it } } }
            start()
        }

        val oldLp = vStatusBar.layoutParams as RelativeLayout.LayoutParams
        val newLp = oldLp.apply { setMargins(0, getStatusBarHeight(), 0, 0) }
        vStatusBar.layoutParams = newLp

        bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(p0: View, p1: Int) { }
            override fun onSlide(p0: View, p1: Float) {
                bSlide.alpha = 1 - p1
                vToolbar.alpha = Math.max(0f, p1 - 0.75f) * 4
            }
        })
        bSlide.onClick("terakhir dibaca. geser ke atas untuk membuka") { }

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
        val lm = rvAyat.layoutManager as LinearLayoutManager
        if (lastReadPosition < lQuran.size) {
            lm.scrollToPosition(lastReadPosition)
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
