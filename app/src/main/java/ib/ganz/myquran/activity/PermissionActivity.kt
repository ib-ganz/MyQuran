package ib.ganz.myquran.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import ib.ganz.myquran.R
import ib.ganz.myquran.kotlinstuff.beGone
import ib.ganz.myquran.kotlinstuff.beVisible
import ib.ganz.myquran.kotlinstuff.extfun.d
import ib.ganz.myquran.kotlinstuff.extfun.requestLocationPermission
import ib.ganz.myquran.kotlinstuff.extfun.requestStoragePermission
import ib.ganz.myquran.manager.PrefManager
import kotlinx.android.synthetic.main.activity_permission.*
import java.util.*

class PermissionActivity : AppCompatActivity() {

    companion object {
        fun go(c: Activity) {
            c.startActivity(Intent(c, PermissionActivity::class.java))
            c.finish()
        }
    }

    private lateinit var tts: TextToSpeech
    private var indoAvailable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        tts = TextToSpeech(this, onInit)

        setBtn()
        bIzinkanLokasi.setOnClickListener { requestLocationPermission() }
        bIzinkanStorage.setOnClickListener { requestStoragePermission() }
        bNext.setOnClickListener {
            if (indoAvailable) MainActivity.go(this)
            else TTSIndoActivity.go(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::tts.isInitialized) {
            tts.shutdown()
        }
    }

    private val onInit = TextToSpeech.OnInitListener {
        if (it == TextToSpeech.SUCCESS && PrefManager.getSuara()) {
            val localeList = mutableListOf<Locale>()
            Locale.getAvailableLocales().forEach { l ->
                val r = tts.isLanguageAvailable(l)
                if (r == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                    localeList.add(l)
                }
            }
            speak { "" }
            indoAvailable = localeList.find { l -> l.language == "in" } != null
        }
    }

    fun speak(id: String? = null, s: () -> String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val params = Bundle()
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "")
            tts.speak(s(), TextToSpeech.QUEUE_FLUSH, params, id)
        }
        else {
            val hm = HashMap<String, String>()
            hm[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = id ?: ""
            tts.speak(s(), TextToSpeech.QUEUE_FLUSH, hm)
        }
    }

    private fun setBtn() {
        var b1 = false
        var b2 = false

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            tLokasiDiizinkan.beGone()
            bIzinkanLokasi.beVisible()
        }
        else {
            tLokasiDiizinkan.beVisible()
            bIzinkanLokasi.beGone()
            b1 = true
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            tStorageDiizinkan.beGone()
            bIzinkanStorage.beVisible()
        }
        else {
            tStorageDiizinkan.beVisible()
            bIzinkanStorage.beGone()
            b2 = true
        }

        bNext.isEnabled = b1 && b2
        d { "bNext.isEnabled: ${bNext.isEnabled}" }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        setBtn()
    }
}
