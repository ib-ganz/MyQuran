package ib.ganz.myquran.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.location.LocationServices
import ib.ganz.myquran.R
import ib.ganz.myquran.helper.NewLocaleHelper
import ib.ganz.myquran.kotlinstuff.beFullScreen
import ib.ganz.myquran.manager.PrefManager
import java.util.*

class SplashActivity : AppCompatActivity() {

    private val fusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NewLocaleHelper.setLocale(this, "in")
        setContentView(R.layout.activity_splash)
        beFullScreen()

        if (
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            Handler().postDelayed({ PermissionActivity.go(this) }, 1000)
        }
        else {
            tts = TextToSpeech(this, onInit)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::tts.isInitialized) {
            tts.shutdown()
        }
    }

    private val onInit = TextToSpeech.OnInitListener {
        if (it == TextToSpeech.SUCCESS || PrefManager.getSkipTTSLang()) {
            val localeList = mutableListOf<Locale>()
            Locale.getAvailableLocales().forEach { l ->
                val r = tts.isLanguageAvailable(l)
                if (r == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                    localeList.add(l)
                }
            }

            if (localeList.find { l -> l.language == "in" } != null) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    MainActivity.go(this)
                }
            }
            else {
                TTSIndoActivity.go(this)
            }
        }
        else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                MainActivity.go(this)
            }
        }
    }
}
