package ib.ganz.myquran.activity

import android.R.id.message
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import ib.ganz.myquran.R
import ib.ganz.myquran.kotlinstuff.extfun.toast
import kotlinx.android.synthetic.main.activity_info.*
import java.net.URLEncoder


class InfoActivity : BaseActivity() {

    companion object {
        fun go(c: Context) = c.startActivity(Intent(c, InfoActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_NoActionBarMarine)
        setContentView(R.layout.activity_info)

        bBack.onClick("kembali") { finish() }
        bWa1.setOnClickListener { sendWa("6281328122219") }
        bWa2.setOnClickListener { sendWa("6281329929810") }
    }

    private fun sendWa(no: String) {
        vibrate()
        val packageManager: PackageManager = packageManager
        val i = Intent(Intent.ACTION_VIEW)

        try {
            val url = "https://api.whatsapp.com/send?phone=$no&text=" + URLEncoder.encode("", "UTF-8")
            i.setPackage("com.whatsapp")
            i.data = Uri.parse(url)
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i)
            }
        }
        catch (e: PackageManager.NameNotFoundException) {
            toast { "Whatsapp tidak terinstall" }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
