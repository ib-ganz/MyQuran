package ib.ganz.myquran.helper

import android.media.MediaPlayer
import android.util.Log
import ib.ganz.myquran.database.AyatData
import java.io.File


object Mp3Player {

    private var mp: MediaPlayer? = null
    private var onDone: (() -> Unit)? = null

    suspend fun play(
        d: AyatData,
        onStart: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) : Mp3Player {
        Mp3Downloader.download(
            d,
            { f ->
                playMp3(f, d)
                onStart?.invoke()
            },
            { onError?.invoke("tidak bisa memutar audio ayat ini") }
        )
        return this
    }

    fun onDone(onDone: (() -> Unit)? = null) {
        this.onDone = onDone
    }

    private suspend fun playMp3(
        file: File,
        d: AyatData,
        onStart: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        try {
            mp = MediaPlayer().apply {
                setOnCompletionListener { onDone?.invoke() }
                setDataSource(file.path)
                prepare()
                start()
            }
        }
        catch (e: Exception) {
            Log.d("logqu", e.message)
            Mp3Downloader.download(
                d,
                { f ->
                    playMp3(f, d)
                    onStart?.invoke()
                },
                { onError?.invoke("tidak bisa memutar audio ayat ini") },
                true
            )
        }
    }

    fun stop() {
        mp?.run {
            stop()
            release()
        }
        mp = null
    }
}

//object SoundManager {
//
////    suspend fun play(
////        d: AyatData,
////        onStart: (() -> Unit)? = null,
////        onError: ((String) -> Unit)? = null
////    ) {
////        Mp3Downloader.download(
////            d,
////            { f ->
////                playMp3(f, d)
////                onStart?.invoke()
////            },
////            { onError?.invoke("tidak bisa memutar audio ayat ini") }
////        )
////    }
//    private var soundPool: SoundPool? = null
//    private var sm = mutableListOf<Int>()
//
//    fun initSound(c: Context) {
//        val maxStreams = 1
//        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            SoundPool.Builder()
//                .setMaxStreams(maxStreams)
//                .build()
//        }
//        else {
//            SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 0)
//        }
//
//        sm.add(soundPool.load())
//
//        sm!![0] = soundPool!!.load(c, R.raw.sound_1, 1)
//        sm!![1] =
//            soundPool!!.load(c, R.raw.sound_2, 1)
//        sm!![2] =
//            soundPool!!.load(c, R.raw.sound_3, 1)
//    }
//
//    fun playSound(sound: Int) {
//        soundPool!!.play(
//            sm!![sound],
//            1f,
//            1f,
//            1,
//            0,
//            1f
//        )
//    }
//
//    fun cleanUpIfEnd() {
//        sm.clear()
//        soundPool!!.release()
//        soundPool = null
//    }
//}