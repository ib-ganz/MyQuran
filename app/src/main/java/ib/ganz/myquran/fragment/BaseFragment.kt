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
import ib.ganz.myquran.kotlinstuff.click
import ib.ganz.myquran.kotlinstuff.then
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

        tts = TextToSpeech(activity, TextToSpeech.OnInitListener {
            (it == TextToSpeech.SUCCESS).then { onTtsReady() }
        }).apply { language = Locale("ind") }

        a = activity as BaseActivity
        job = Job()
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

    protected fun speak(s: () -> String) {
        tts.speak(s(), TextToSpeech.QUEUE_FLUSH, null)
    }

    protected fun View.onClick(speech: String, onEveryClick: (() -> Unit)? = null, f: (() -> Unit)? = null) =
        this.onClick(speech, -1, onEveryClick, f)

    protected fun View.onClick(speech: String, listPosition: Int = -1, onEveryClick: (() -> Unit)? = null, f: (() -> Unit)? = null) = click {
        val id = this.javaClass.simpleName + this.id + listPosition
        if (clickedView == id) {
            clickedView = ""
            f?.invoke()
        }
        else {
            clickedView = id
            speak { speech }
        }
        onEveryClick?.invoke()
        vibrate()
    }
}