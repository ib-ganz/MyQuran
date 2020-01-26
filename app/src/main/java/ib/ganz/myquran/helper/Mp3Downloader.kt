package ib.ganz.myquran.helper

import ib.ganz.myquran.network.ApiClient
import ib.ganz.myquran.network.IServices
import okhttp3.ResponseBody

object Mp3Downloader {

    private val service by lazy { ApiClient.retrofit.create(IServices::class.java) }

    suspend fun download(noSurat: String, noAyat: String): ResponseBody = service.download(noSurat, noAyat)
}