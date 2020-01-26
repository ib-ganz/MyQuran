package ib.ganz.myquran.customclass

import android.widget.ImageView
import ib.ganz.myquran.R
import ib.ganz.myquran.kotlinstuff.DateQu
import ib.ganz.myquran.kotlinstuff.fromAmPm
import ib.ganz.myquran.kotlinstuff.setHour
import ib.ganz.myquran.kotlinstuff.setMinute

object BackgroundManager {

    fun init(i: ImageView, j: JadwalItem) {
//        val now = DateQu.now().toCalendar()
//
//        fun isSubuh(): Boolean {
//            val start = DateQu.now().toCalendar().apply {
//                setHour(j.fajr.fromAmPm().toHour().toInt())
//                setMinute(j.fajr.fromAmPm().toMinute().toInt())
//            }
//            val end = DateQu.now().toCalendar().apply {
//                setHour(j.shurooq.fromAmPm().toHour().toInt())
//                setMinute(j.shurooq.fromAmPm().toMinute().toInt())
//            }
//            return now.before(end) && now.after(start)
//        }
//
//        fun isSyuruq(): Boolean {
//            val start = DateQu.now().toCalendar().apply {
//                setHour(j.shurooq.fromAmPm().toHour().toInt())
//                setMinute(j.shurooq.fromAmPm().toMinute().toInt())
//            }
//            val end = DateQu.now().toCalendar().apply {
//                setHour(j.dhuhr.fromAmPm().toHour().toInt())
//                setMinute(j.dhuhr.fromAmPm().toMinute().toInt())
//            }
//            return now.before(end) && now.after(start)
//        }
//
//        fun isDzuhur(): Boolean {
//            val start = DateQu.now().toCalendar().apply {
//                setHour(j.dhuhr.fromAmPm().toHour().toInt())
//                setMinute(j.dhuhr.fromAmPm().toMinute().toInt())
//            }
//            val end = DateQu.now().toCalendar().apply {
//                setHour(j.asr.fromAmPm().toHour().toInt())
//                setMinute(j.asr.fromAmPm().toMinute().toInt())
//            }
//            return now.before(end) && now.after(start)
//        }
//
//        fun isAshar(): Boolean {
//            val start = DateQu.now().toCalendar().apply {
//                setHour(j.asr.fromAmPm().toHour().toInt())
//                setMinute(j.asr.fromAmPm().toMinute().toInt())
//            }
//            val end = DateQu.now().toCalendar().apply {
//                setHour(j.maghrib.fromAmPm().toHour().toInt())
//                setMinute(j.maghrib.fromAmPm().toMinute().toInt())
//            }
//            return now.before(end) && now.after(start)
//        }
//
//        fun isMaghrib(): Boolean {
//            val start = DateQu.now().toCalendar().apply {
//                setHour(j.maghrib.fromAmPm().toHour().toInt())
//                setMinute(j.maghrib.fromAmPm().toMinute().toInt())
//            }
//            val end = DateQu.now().toCalendar().apply {
//                setHour(j.isha.fromAmPm().toHour().toInt())
//                setMinute(j.isha.fromAmPm().toMinute().toInt())
//            }
//            return now.before(end) && now.after(start)
//        }
//
//        fun isIsya(): Boolean {
//            val start = DateQu.now().toCalendar().apply {
//                setHour(j.isha.fromAmPm().toHour().toInt())
//                setMinute(j.isha.fromAmPm().toMinute().toInt())
//            }
//            val end = DateQu.now().toCalendar().apply {
//                setHour(j.shurooq.fromAmPm().toHour().toInt())
//                setMinute(j.shurooq.fromAmPm().toMinute().toInt())
//            }
//            return now.before(end) && now.after(start)
//        }
//
//        when {
//            isSubuh() -> i.setImageResource(R.drawable.bg_0)
//            isSyuruq() -> i.setImageResource(R.drawable.bg_1)
//            isDzuhur() -> i.setImageResource(R.drawable.bg_2)
//            isAshar() -> i.setImageResource(R.drawable.bg_3)
//            isMaghrib() -> i.setImageResource(R.drawable.bg_4)
//            isIsya() -> i.setImageResource(R.drawable.bg_5)
//            else -> i.setImageResource(R.drawable.bg_5)
//        }
    }
}