package ib.ganz.myquran.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import ib.ganz.myquran.R
import ib.ganz.myquran.fragment.SearchKataFragment
import ib.ganz.myquran.fragment.SearchSuaraFragment
import ib.ganz.myquran.helper.Mp3Player
import kotlinx.android.synthetic.main.activity_pencarian.*


class PencarianActivity : BaseActivity() {

    companion object {
        fun go(c: Context) = c.startActivity(Intent(c, PencarianActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pencarian)

        val adapter = FragmentPagerItemAdapter(supportFragmentManager, FragmentPagerItems.with(this).run {
            add("Suara", SearchSuaraFragment::class.java)
            add("Kata", SearchKataFragment::class.java)
            create()
        })

        vpPencarian.adapter = adapter
        tlPencarian.setViewPager(vpPencarian)
        vpPencarian.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) { }
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) { }
            override fun onPageSelected(p0: Int) {
                vibrate()
                Mp3Player.stop()
                if (p0 == 0) {
                    speak { "pencarian ayat berdasarkan suara" }
                }
                else {
                    speak { "pencarian ayat berdasarkan kata" }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Mp3Player.stop()
    }

    override fun onTtsReady() {
        speak { "apa sih kak?" }
//        speak { "anda masuk di halaman pencarian ayat berdasarkan suara. geser ke kanan untuk masuk di halaman pencarian ayat berdasarkan kata." }
    }
}
