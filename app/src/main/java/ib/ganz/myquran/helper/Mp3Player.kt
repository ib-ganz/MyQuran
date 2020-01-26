package ib.ganz.myquran.helper

import android.media.MediaPlayer
import android.os.Environment
import android.util.Log
import ib.ganz.myquran.database.AyatData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

object Mp3Player {

    private var mp: MediaPlayer? = null

    suspend fun play(d: AyatData, onError: (String) -> Unit) {
        val noSurat = d.idSurat.padStart(3, '0')
        val noAyat = d.nomorAyat.padStart(3, '0')
        val fileName = "$noSurat$noAyat.mp3"
        val file = File("${Environment.getExternalStorageDirectory()}${File.separator}$fileName")

        if (file.exists()) {
            playMp3(file)
        }
        else {
            try {
                val res: ResponseBody = Mp3Downloader.download(noSurat, noAyat)
                writeResponseBodyToDisk(res, file)
                playMp3(file)
            }
            catch (e: Exception) {
                onError("tidak bisa memutar audio ayat ini")
                Log.d("Mp3Player", e.message)
            }
        }
    }

    private fun playMp3(file: File) {
        mp = MediaPlayer().apply {
            setDataSource(file.path)
            prepare()
            start()
        }
    }

    fun stop() {
        mp?.run {
            stop()
            release()
        }
        mp = null
    }

    private suspend fun writeResponseBodyToDisk(body: ResponseBody, file: File) = withContext(Dispatchers.IO) {
        try {
            val input = body.byteStream()
            val output = FileOutputStream(file)
            val data = ByteArray(1024)
            var total: Long = 0
            var count = 0

            while (count != -1) {
                count = input.read(data)
                if (count == -1) break

                total += count.toLong()
                output.write(data, 0, count)
            }

            output.flush()
            output.close()
            input.close()
        }
        catch (e: Throwable) {
            Log.d("Mp3Player", e.message!!)
        }
    }
}