package ib.ganz.myquran.customclass

import com.google.gson.annotations.SerializedName

class JadwalItem(
    @SerializedName("date_for") val dateFor: String,
    @SerializedName("fajr")     val fajr: String,
    @SerializedName("shurooq")  val shurooq: String,
    @SerializedName("dhuhr")    val dhuhr: String,
    @SerializedName("asr")      val asr: String,
    @SerializedName("maghrib")  val maghrib: String,
    @SerializedName("isha")     val isha: String
)

class JadwalData(
    @SerializedName("status_valid") val statusValid: String,
    @SerializedName("status_code")  val statusCode: String,
    @SerializedName("items")        val items: MutableList<JadwalItem>
)