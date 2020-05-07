package ib.ganz.myquran.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import ib.ganz.myquran.R
import ib.ganz.myquran.database.AyatData
import ib.ganz.myquran.helper.ExitManager
import ib.ganz.myquran.helper.LastReadHelper
import ib.ganz.myquran.kotlinstuff.extfun.reqStorageAndLocationPermissions
import ib.ganz.myquran.kotlinstuff.fromJsonObject
import ib.ganz.myquran.kotlinstuff.getStatusBarHeight
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
    private val lastReadHelper by lazy { LastReadHelper(this, this, findViewById<RelativeLayout>(R.id.bottomSheet)) }

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
        bInfo.onClick("tentang aplikasi") { InfoActivity.go(a) }
        bSetting.onClick("pengaturan") { ListSettingActivity.go(a) }
        bCompass.onClick("kompas") { CompassActivity.go(a) }
        bSearch.onClick("pencarian ayat") { PencarianActivity.go(a) }
        bTafsir.onClick("kajian tafsir") { ListSuratActivity.go(a) }
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
            override fun onStateChanged(p0: View, p1: Int) {
                if (p1 == BottomSheetBehavior.STATE_HIDDEN || p1 == BottomSheetBehavior.STATE_COLLAPSED) {
                    lastReadHelper.stop()
                    changeStatusBarColor(ContextCompat.getColor(a, R.color.colorPrimaryDark))
                }
                else if (p1 == BottomSheetBehavior.STATE_EXPANDED) {
                    changeStatusBarColor(ContextCompat.getColor(a, R.color.colorMarineDark))
                }
            }
            override fun onSlide(p0: View, p1: Float) {
                bSlide.alpha = 1 - p1
                vToolbar.alpha = Math.max(0f, p1 - 0.75f) * 4
            }
        })
        bSlide.onClick("terakhir dibaca. geser ke atas untuk membuka") { }
        bSearchSound.onClick("Cari ayat") {
            lastReadHelper.stop()
            SearchSoundActivity.goWithResult(this)
        }

        lastReadHelper.help()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 666 && resultCode == Activity.RESULT_OK && data != null) {
            lastReadHelper.onSoundSelected(data.getStringExtra("res").fromJsonObject(AyatData::class.java))
        }
    }

    override fun onBackPressed() {
        if (bottomSheet.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
            lastReadHelper.stop()
        }
        else {
            ExitManager.go(a)
        }
    }

    override fun onResume() {
        super.onResume()
        lastReadHelper.onResume()
    }

    override fun onPause() {
        super.onPause()
        lastReadHelper.onPause()
    }

    private fun changeStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }
}
