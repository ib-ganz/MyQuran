package ib.ganz.myquran.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.*
import android.speech.tts.TextToSpeech
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import ib.ganz.myquran.helper.LocaleHelper
import ib.ganz.myquran.helper.NewLocaleHelper
import ib.ganz.myquran.kotlinstuff.click
import ib.ganz.myquran.kotlinstuff.then
import ib.ganz.myquran.manager.PrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import kotlin.coroutines.CoroutineContext


/**
 * Created by limakali on 3/17/2018.
 */

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private lateinit var progressDialog: ProgressDialog
    private lateinit var fm: FragmentManager
    private lateinit var tts: TextToSpeech
    protected lateinit var a: BaseActivity
    private var clickedView: String = ""
    private lateinit var currentWaiter: Waiter

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(NewLocaleHelper.setLocale(newBase, "in"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        NewLocaleHelper.onAttach(this)
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(this)
        fm = supportFragmentManager
        a = this
        job = Job()
        tts = TextToSpeech(a, onInit)
    }

    private val onInit = TextToSpeech.OnInitListener {
        if (it == TextToSpeech.SUCCESS && PrefManager.getSuara()) {

            val localeList = mutableListOf<Locale>()
            val idLikes = mutableListOf<Locale>()
            Locale.getAvailableLocales().forEach { l ->
                val r = tts.isLanguageAvailable(l)
                if (r == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                    localeList.add(l)
                    if (l.language == "in" || l.language == "id" || l.language == "idn" || l.language == "ind") {
                        idLikes.add(l)
                    }
                }
            }
//                d { localeList.joinToString { l -> l.language } }
//                d { idLikes.joinToString { l -> "${l.language} ${l.displayLanguage} ${l.country} ${l.displayCountry}" } }
//                d { "${localeList.size}" }
//                d { "${idLikes.size}" }

            if (localeList.find { l -> l.language == "in" } == null) {
                AlertDialog.Builder(a)
                    .setMessage("Bahasa Indonesia untuk Text To Speech tidak tersedia. Apakah anda ingin mendownload?")
                    .setPositiveButton("Iya") { v, i ->
                        val installIntent = Intent()
                        installIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
                        startActivity(installIntent)
                    }
                    .setNegativeButton("Tidak", null)
                    .show()
            }
            tts.setLanguage(LocaleHelper.getLocale())
            onTtsReady()
        }
    }

    fun speak(id: String? = null, s: () -> String) {
        if (!PrefManager.getSuara()) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val params = Bundle()
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "")
            tts.speak(s().adaptToTts(), TextToSpeech.QUEUE_FLUSH, params, id)
        }
        else {
            val hm = HashMap<String, String>()
            hm[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = id ?: ""
            tts.speak(s().adaptToTts(), TextToSpeech.QUEUE_FLUSH, hm)
        }

        if (::currentWaiter.isInitialized && !currentWaiter.isCancelled) {
            currentWaiter.cancel(true)
        }
        currentWaiter = Waiter(isPlaying, tts, onTtsDone).apply { execute() }
    }

    private fun String.adaptToTts(): String {
        return this
            .replace("'", "")
            .replace("Q.S.", " quran surat ", true)
            .replace("/", " atau ")
            .replace("saw", "salallahu alaihi wasallam", true)
            .replace("swt", "subhanahu wata'ala", true)
    }

    protected open fun onTtsReady() { }

    protected open val onTtsDone = { }

    protected open val isPlaying = { true }

    protected fun stopTts() {
        tts.stop()
    }

    // ############################## CLICK ######################################

    fun vibrate() {
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v.vibrate(longArrayOf(0, 40), -1)
        }
    }

    protected fun View.onClick(
        speech: String,
        onEveryClick: (() -> Unit)? = null,
        onAction: (() -> Unit)? = null
    ) = this.onClick(speech, -1, onEveryClick, onAction)

    protected fun View.onClick(
        speech: String,
        listPosition: Int = -1,
        onEveryClick: (() -> Unit)? = null,
        onAction: (() -> Unit)? = null
    ) = click {
        when (PrefManager.getButtonMode()) {
            PrefManager.MODE_1 -> {
                val id = this.javaClass.simpleName + this.id + listPosition
                if (clickedView == id) {
                    clickedView = ""
                    onAction?.invoke()
                }
                else {
                    clickedView = id
                    speak { speech }
                }
                onEveryClick?.invoke()
                vibrate()
            }
            PrefManager.MODE_2 -> {
                val id = this.javaClass.simpleName + this.id + listPosition
                if (clickedView == id) {
                    clickedView = ""
                    onAction?.invoke()
                }
                else {
                    clickedView = id
                    speak { speech }
                }
                onEveryClick?.invoke()
            }
            PrefManager.MODE_3 -> {
                val id = this.javaClass.simpleName + this.id + listPosition
                if (clickedView == id) {
                    clickedView = ""
                    onAction?.invoke()
                }
                else {
                    clickedView = id
                }
                onEveryClick?.invoke()
                vibrate()
            }
            PrefManager.MODE_4 -> {
                onAction?.invoke()
                onEveryClick?.invoke()
                speak { speech }
                vibrate()
            }
            PrefManager.MODE_5 -> {
                onAction?.invoke()
                onEveryClick?.invoke()
                speak { speech }
            }
            PrefManager.MODE_6 -> {
                onAction?.invoke()
                onEveryClick?.invoke()
                vibrate()
            }
            PrefManager.MODE_7 -> {
                onAction?.invoke()
                onEveryClick?.invoke()
            }
        }
    }

    // ##########################################################################

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        if (::tts.isInitialized) {
            tts.shutdown()
        }
    }

    // ###########################

    class Waiter(
        private val isPlaying: () -> Boolean,
        private val tts: TextToSpeech,
        private val handler: () -> Unit
    ) : AsyncTask<Void?, Void?, Void?>() {

        override fun doInBackground(vararg voids: Void?): Void? {
            while (tts.isSpeaking) {
                try {
                    Thread.sleep(100)
                }
                catch (e: Exception) { }
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            isPlaying().then(handler)
        }
    }
}
