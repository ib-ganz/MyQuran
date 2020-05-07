package ib.ganz.myquran.customclass

import com.google.gson.annotations.SerializedName
import ib.ganz.myquran.kotlinstuff.*
import java.util.*

class JadwalItem(
    @SerializedName("date_for") val dateFor: String,
    @SerializedName("fajr")     val fajr: String,
    @SerializedName("shurooq")  val shurooq: String,
    @SerializedName("dhuhr")    val dhuhr: String,
    @SerializedName("asr")      val asr: String,
    @SerializedName("maghrib")  val maghrib: String,
    @SerializedName("isha")     val isha: String
) {
    fun subuhStart() = now().toCalendar().apply {
        setHour(fajr.fromAmPm().toHour().toInt())
        setMinute(fajr.fromAmPm().toMinute().toInt())
    }
    fun subuhEnd() = now().toCalendar().apply {
        setHour(shurooq.fromAmPm().toHour().toInt())
        setMinute(shurooq.fromAmPm().toMinute().toInt())
    }

    fun dzuhurStart() = now().toCalendar().apply {
        setHour(dhuhr.fromAmPm().toHour().toInt())
        setMinute(dhuhr.fromAmPm().toMinute().toInt())
    }
    fun dzuhurEnd() = now().toCalendar().apply {
        setHour(asr.fromAmPm().toHour().toInt())
        setMinute(asr.fromAmPm().toMinute().toInt())
    }

    fun asharStart() = now().toCalendar().apply {
        setHour(asr.fromAmPm().toHour().toInt())
        setMinute(asr.fromAmPm().toMinute().toInt())
    }
    fun asharEnd() = now().toCalendar().apply {
        setHour(maghrib.fromAmPm().toHour().toInt())
        setMinute(maghrib.fromAmPm().toMinute().toInt())
    }

    fun maghribStart() = now().toCalendar().apply {
        setHour(maghrib.fromAmPm().toHour().toInt())
        setMinute(maghrib.fromAmPm().toMinute().toInt())
    }
    fun maghribEnd() = now().toCalendar().apply {
        setHour(isha.fromAmPm().toHour().toInt())
        setMinute(isha.fromAmPm().toMinute().toInt())
    }

    fun isyaStart() = now().toCalendar().apply {
        val curHour = getHour()
        setHour(isha.fromAmPm().toHour().toInt())
        setMinute(isha.fromAmPm().toMinute().toInt())

        if (curHour < 5) {
            add(Calendar.HOUR, -24)
        }
    }
    fun isyaEnd() = now().toCalendar().apply {
        val curHour = getHour()
        setHour(fajr.fromAmPm().toHour().toInt())
        setMinute(fajr.fromAmPm().toMinute().toInt())

        if (curHour > 5) {
            add(Calendar.HOUR, 24)
        }
    }
}

class JadwalData(
    @SerializedName("status_valid") val statusValid: String,
    @SerializedName("status_code")  val statusCode: String,
    @SerializedName("items")        val items: MutableList<JadwalItem>
)