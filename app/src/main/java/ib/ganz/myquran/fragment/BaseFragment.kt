package ib.ganz.myquran.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ib.ganz.myquran.activity.BaseActivity
import ib.ganz.myquran.helper.LocaleHelper
import ib.ganz.myquran.kotlinstuff.click
import ib.ganz.myquran.kotlinstuff.then
import ib.ganz.myquran.manager.PrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import kotlin.coroutines.CoroutineContext

open class BaseFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    protected lateinit var a: BaseActivity
    private lateinit var tts: TextToSpeech
    private var clickedView: String = ""

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        a = activity as BaseActivity
        job = Job()

        val onInit = TextToSpeech.OnInitListener {
            if (it == TextToSpeech.SUCCESS && PrefManager.getSuara()) {
                tts.language = LocaleHelper.getLocale()
                onTtsReady()
            }
        }
        tts = TextToSpeech(a, onInit)
    }

    protected open fun onTtsReady() { }

    protected fun vibrate() {
        val v = activity!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v.vibrate(longArrayOf(0, 40), -1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown()
    }

    protected fun speak(id: String? = null, s: () -> String) {
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
    }

    private fun String.adaptToTts(): String {
        return this
            .replace("'", "")
            .replace("Q.S.", " quran surat ", true)
            .replace("/", " atau ")
            .replace("saw", "salallahu alaihi wasallam", true)
            .replace("swt", "subhanahu wata'ala", true)
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
}