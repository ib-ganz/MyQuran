package ib.ganz.myquran.network

import ib.ganz.myquran.manager.PrefManager
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface IServices {

    @GET("https://download.quranicaudio.com/verses/{qari}/mp3/{noSurat}{noAyat}.mp3")
    @Streaming
    suspend fun download(
        @Path("noSurat") noSurat: String,
        @Path("noAyat") noAyat: String,
        @Path("qari") qari: String = PrefManager.getQari()
    ): ResponseBody
}