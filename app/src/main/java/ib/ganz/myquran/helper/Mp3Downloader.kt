package ib.ganz.myquran.helper

import android.os.Environment
import android.util.Log
import ib.ganz.myquran.database.AyatData
import ib.ganz.myquran.manager.PrefManager
import ib.ganz.myquran.network.ApiClient
import ib.ganz.myquran.network.IServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

object Mp3Downloader {

    private val service by lazy { ApiClient.retrofit.create(IServices::class.java) }

    suspend fun download(
        d: AyatData,
        onSuccess: (suspend (File) -> Unit)? = null,
        onError: ((String) -> Unit)? = null,
        force: Boolean = false
    ) {
        val noSurat = d.idSurat.padStart(3, '0')
        val noAyat = d.nomorAyat.padStart(3, '0')
        val fileName = "$noSurat$noAyat.mp3"
        val sep = File.separator
        val dir = "${Environment.getExternalStorageDirectory()}${sep}TafsirForBlind${sep}${PrefManager.getQari().replace("/", "_")}${sep}"
        val dirFile = File(dir)
        val file = File("$dir$fileName")

        if (!dirFile.exists()) {
            dirFile.mkdirs()
        }

        if (file.exists() && !force) {
            onSuccess?.invoke(file)
        }
        else {
            try {
                val res: ResponseBody = download(noSurat, noAyat)
                if (!file.exists()) {
                    writeResponseBodyToDisk(res, file)
                }
                onSuccess?.invoke(file)
            }
            catch (e: Exception) {
                Log.d("Mp3Player", e.message)
                onError?.invoke(e.message ?: "error downloading mp3")
            }
        }
    }

    private suspend fun download(noSurat: String, noAyat: String): ResponseBody = service.download(noSurat, noAyat)

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