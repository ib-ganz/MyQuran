package ib.ganz.myquran.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.app.AppCompatActivity
import ib.ganz.myquran.R
import ib.ganz.myquran.manager.PrefManager
import kotlinx.android.synthetic.main.activity_t_t_s_indo.*
import java.util.*


class TTSIndoActivity : AppCompatActivity() {

    companion object {
        fun go(c: Activity) {
            c.startActivity(Intent(c, TTSIndoActivity::class.java))
            c.finish()
        }
    }

    private lateinit var tts: TextToSpeech
    private var reallyResuming = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_t_t_s_indo)

        bLewati.setOnClickListener {
            if (cbLewati.isChecked) {
                PrefManager.setSkipTTSLang(true)
            }
            MainActivity.go(this)
        }
        bAtur.setOnClickListener {
            val installIntent = Intent()
            installIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
            startActivity(installIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::tts.isInitialized) {
            tts.shutdown()
        }
    }

    override fun onPause() {
        super.onPause()
        reallyResuming = true
    }

    override fun onResume() {
        super.onResume()
        if (reallyResuming) {
            tts = TextToSpeech(this, onInit)
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
                MainActivity.go(this)
            }
        }
        else {
            MainActivity.go(this)
        }
    }
}
