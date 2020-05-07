package ib.ganz.myquran.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import ib.ganz.myquran.R
import ib.ganz.myquran.kotlinstuff.beVisible
import ib.ganz.myquran.manager.PrefManager
import kotlinx.android.synthetic.main.activity_list_setting.*

class ListSettingActivity : BaseActivity() {

    companion object {
        fun go(c: Context) = c.startActivity(Intent(c, ListSettingActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_setting)

        val open = { i: Int -> SettingActivity.go(a, i) }

        bTombol.onClick("pengaturan tombol") { open(SettingActivity.PENGATURAN_TOMBOL) }
        bSuara.onClick("Suara") { open(SettingActivity.SUARA) }
        bQari.onClick("qari'") { open(SettingActivity.QARI) }
        bQuran.onClick("qur'an") { open(SettingActivity.QURAN) }
        bTerjemah.onClick("terjemah") { open(SettingActivity.TERJEMAH) }
        bTafisr.onClick("tafsir") { open(SettingActivity.TAFSIR) }
        bBack.onClick("kembali") { finish() }
        bReset.onClick("Reset pengaturan ke default") {
            PrefManager.reset()
            iCheck.beVisible()
        }
    }
}
