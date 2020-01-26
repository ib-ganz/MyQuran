package ib.ganz.myquran.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by limakali on 1/5/2018.
 */

object ApiClient {

    private const val ROOT = "http://sipp-on.com/"
    const val BASE_URL = ROOT + "api/"
    const val IMG_DIR = ROOT + "images/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GxonConverterFaxtory.create())
            .build()
    }

    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .build()
    }

}
