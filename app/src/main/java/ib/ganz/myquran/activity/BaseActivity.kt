package ib.ganz.myquran.activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import ib.ganz.myquran.kotlinstuff.click
import ib.ganz.myquran.kotlinstuff.then
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(this)
        fm = supportFragmentManager
        a = this
        job = Job()

        tts = TextToSpeech(a, TextToSpeech.OnInitListener {
            (it == TextToSpeech.SUCCESS).then { onTtsReady() }
        }).apply { language = Locale("ind") }
    }

    protected open fun onTtsReady() { }

    protected fun vibrate() {
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v.vibrate(longArrayOf(0, 40), -1)
        }
    }

    protected fun speak(s: () -> String) {
        tts.speak(s(), TextToSpeech.QUEUE_FLUSH, null)
    }

    protected fun View.onClick(speech: String, f: (() -> Unit)? = null) = click {
        val id = this.javaClass.simpleName + this.id
        if (clickedView == id) {
            clickedView = ""
            f?.invoke()
        }
        else {
            clickedView = id
            speak { speech }
        }
        vibrate()
    }

    // ##########################################################################

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateBaseContextLocale(base))
    }

    private fun updateBaseContextLocale(context: Context): Context {
        val language = "in"
        val locale = Locale(language)
        Locale.setDefault(locale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResourcesLocale(context, locale)
        } else updateResourcesLocaleLegacy(context, locale)

    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResourcesLocale(context: Context, locale: Locale): Context {
        val configuration = context.getResources().getConfiguration()
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLocaleLegacy(context: Context, locale: Locale): Context {
        val resources = context.getResources()
        val configuration = resources.getConfiguration()
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.getDisplayMetrics())
        return context
    }
}
