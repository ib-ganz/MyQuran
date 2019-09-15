package ib.ganz.myquran.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import ib.ganz.myquran.R
import ib.ganz.myquran.fragment.SearchKataFragment
import ib.ganz.myquran.fragment.SearchSuaraFragment
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
    }
}
