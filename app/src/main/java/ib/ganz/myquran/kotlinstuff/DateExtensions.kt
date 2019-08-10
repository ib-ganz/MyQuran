@file:kotlin.jvm.JvmName("XDate")
@file:kotlin.jvm.JvmMultifileClass

package ib.ganz.myquran.kotlinstuff

import ib.ganz.sipp_on.kotlinstuff.then
import org.joda.time.*
import java.util.*
import java.lang.IllegalStateException

fun String.fromSql()        = DateQu.fromSql(this)
fun String.toCalendar()     = fromSql().toCalendar()
fun String.toDate()         = fromSql().toDate()
fun String.toIndoDate()     = fromSql().toIndoDate()
fun String.toIndoDateTime() = fromSql().toIndoDateTime()
fun String.toSqlDate()      = fromSql().toSqlDate()
fun String.toDateOnly()     = fromSql().toDateOnly()
fun String.toDayName()      = fromSql().toDayName()
fun String.toTime()         = fromSql().toTime()
fun String.toHour()         = fromSql().toHour()
fun String.toMinute()       = fromSql().toMinute()
fun String.dayInterval()    = fromSql().dayInterval()
fun String.dayCount()       = fromSql().dayCount()
fun String.notPassed()      = fromSql().notPassed()
fun String.isPassed()       = fromSql().isPassed()
fun String.asPast()         = fromSql().asPast()
fun String.asFuture()       = fromSql().asFuture()
fun String.asElapsed()      = fromSql().asElapsed()

fun now()                       = DateQu.now()
fun Date.dateQu()               = DateQu.fromDate(this)
fun Calendar.dateQu()           = DateQu.fromCalendar(this)
fun Calendar.getYear()          = get(Calendar.YEAR)
fun Calendar.getMonth()         = get(Calendar.MONTH)
fun Calendar.getDayOfMonth()    = get(Calendar.DAY_OF_MONTH)
fun Calendar.getDayOfWeek()     = get(Calendar.DAY_OF_WEEK)
fun Calendar.getHour()          = get(Calendar.HOUR_OF_DAY)
fun Calendar.getMinute()        = get(Calendar.MINUTE)
fun Calendar.setYear(i: Int)    = set(Calendar.YEAR, i)
fun Calendar.setMonth(i: Int)   = set(Calendar.MONTH, i)
fun Calendar.setDayOfMonth(i: Int) = set(Calendar.DAY_OF_MONTH, i)
fun Calendar.toDateQu()         = DateQu.fromCalendar(this)

infix fun Calendar.setHour(i: Int): Calendar {
    set(Calendar.HOUR_OF_DAY, i)
    return this
}
infix fun Calendar.setMinute(i: Int): Calendar {
    set(Calendar.MINUTE, i)
    return this
}

class DateQu(private val cal: Calendar) {

    companion object {
        fun fromSql(s: String)              = DateQu(getCalFromSql(s))
        fun fromIndoDate(s: String)         = DateQu(getCalFromIndoDate(s))
        fun fromDate(d: Date)               = DateQu(getCalFromDate(d))
        fun fromCalendar(c: Calendar)       = DateQu(c)
        fun fromIntArray(i: Array<Int>)     = DateQu(getCalFromIntArray(i))
        fun fromInts(y: Int, m: Int, d: Int)= DateQu(getCalFromIntArray(arrayOf(y, m, d)))
        fun now()                           = DateQu(Calendar.getInstance())

        private fun getCalFromIntArray(i: Array<Int>) = Calendar.getInstance().apply { // y, m, d
            setYear(i[0])
            setMonth(i[1])
            setDayOfMonth(i[2])
        }
        private fun getCalFromDate(d: Date) = Calendar.getInstance().apply {
            setYear(d.year)
            setMonth(d.month)
            setDayOfMonth(d.day)
            setHour(d.hours)
            setMinute(d.minutes)
        }
        private fun getCalFromSql(s: String) = Calendar.getInstance().apply {
            val arr = s.split(" ")
            val dateStringArr = arr[0].split("-")

            (dateStringArr.size != 3 || dateStringArr[1].toIntOrNull() == null) then { throw IllegalStateException("Hey, this is not sql date format") }

            dateStringArr.run {
                setYear(this[0].toInt())
                setMonth(this[1].toInt() - 1)
                setDayOfMonth(this[2].toInt())
            }

            (arr.size == 2) then {
                arr[1].split(":").run {
                    setHour(this[0].toInt())
                    setMinute(this[1].toInt())
                }
            }
        }
        private fun getCalFromIndoDate(s: String) = Calendar.getInstance().apply {
            s.split(" ").run {
                setDayOfMonth(this[0].toInt())
                setMonth(this[1].toInt())
                setYear(this[2].toInt())
            }
        }

        /**
         * Compare two calendar
         * @return 0 if it's the same day
         */
        fun dayInterval(c1: Calendar, c2: Calendar): Int {
            val tz1 = c1.timeZone
            val jodaTz1 = DateTimeZone.forID(tz1.id)
            val dateTime1 = DateTime(c1.timeInMillis, jodaTz1)
            val localDate1 = dateTime1.toLocalDate()

            val tz2 = c2.timeZone
            val jodaTz2 = DateTimeZone.forID(tz2.id)
            val dateTime2 = DateTime(c2.timeInMillis, jodaTz2)
            val localDate2 = dateTime2.toLocalDate()

            return Days.daysBetween(localDate1, localDate2).days
        }

        /**
         * Compare two calendar
         * @return 1 if it's the same day
         */
        fun dayCount(c1: Calendar, c2: Calendar) = dayInterval(c1, c2) + 1
    }

    override fun toString(): String {
        return "${toDayName()}, ${toIndoDateTime()}"
    }

    private val months = arrayListOf("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember")
    private val days = arrayListOf("Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")
    private val pastIntervals by lazy {
        object {
            val day     = dayInterval(toCalendar(), now().toCalendar())
            val hour    = now().toHour() - toHour()
            val minute  = now().toMinute() - toMinute()
        }
    }
    private val futureIntervals by lazy {
        object {
            val day     = dayInterval(toCalendar(), now().toCalendar()) * -1
            val hour    = (now().toHour() - toHour()) * -1
            val minute  = (now().toMinute() - toMinute()) * -1
        }
    }
    private fun Int.addLeading() = if ((this.toString() + "").length == 1) "0$this" else "" + this

    fun toCalendar()    = cal
    fun toDate()        = Date(cal.getYear(), cal.getMonth(), cal.getDayOfMonth())
    fun toIndoDate()    = "${cal.getDayOfMonth()} ${months[cal.getMonth()]} ${cal.getYear()}"
    fun toIndoDateTime()= "${cal.getDayOfMonth()} ${months[cal.getMonth()]} ${cal.getYear()} ${toTime()}"
    fun toSqlDate()     = "${cal.getYear().addLeading()}-${(cal.getMonth() + 1).addLeading()}-${cal.getDayOfMonth().addLeading()}"
    fun toSqlDateTime() = "${cal.getYear().addLeading()}-${(cal.getMonth() + 1).addLeading()}-${cal.getDayOfMonth().addLeading()} ${cal.getHour().addLeading()}:${cal.getMinute().addLeading()}"
    fun toDateOnly()    = cal.getDayOfMonth().toString()
    fun toDayName()     = days[cal.getDayOfWeek() - 1]
    fun toTime()        = "${cal.getHour().addLeading()}:${cal.getMinute().addLeading()}"
    fun toHour()        = cal.getHour()
    fun toMinute()      = cal.getMinute()

    fun dayInterval() = dayInterval(cal, Calendar.getInstance())  //compare now to calendar of this, return 0 if it's the same day
    fun dayCount() = dayInterval(cal, Calendar.getInstance()) + 1 //compare now to calendar of this, return 1 if it's the same day

    fun notPassed() = !isPassed()
    fun isPassed() = now().toCalendar().after(toCalendar())

    fun asPast() = when {
        pastIntervals.day > 28  -> toIndoDate()
        pastIntervals.day > 7   -> "${pastIntervals.day / 7} minggu yang lalu"
        pastIntervals.day > 1   -> "${pastIntervals.day} hari yang lalu"
        pastIntervals.day == 1  -> "Kemarin"
        pastIntervals.day == 0  -> when {
            pastIntervals.hour > 0      -> "${pastIntervals.hour} jam yang lalu"
            pastIntervals.minute > 0    -> "${pastIntervals.minute} menit yang lalu"
            else -> "Baru saja"
        }
        else -> toIndoDate()
    }
    fun asFuture() = when {
        futureIntervals.day > 28  -> toIndoDate()
        futureIntervals.day > 7   -> "${futureIntervals.day / 7} minggu lagi"
        futureIntervals.day > 1   -> "${futureIntervals.day} hari lagi"
        futureIntervals.day == 1  -> "Besok"
        futureIntervals.day == 0  -> when {
            futureIntervals.hour > 0      -> "${futureIntervals.hour} jam lagi"
            futureIntervals.minute > 0    -> "${futureIntervals.minute} menit lagi"
            else -> "Sekarang"
        }
        else -> toIndoDate()
    }
    fun asElapsed() = if (isPassed()) asPast() else asFuture()

    fun howOld(suffix: String = "Tahun"): String {
        val theY = toCalendar()
        val gapY = Years.yearsBetween(LocalDate(theY.getYear(), theY.getMonth(), theY.getDayOfMonth()), LocalDate())

        return "$gapY $suffix"
    }
}

fun String.fromAmPm() = TimeQu.fromAmPm(this)

class TimeQu {
    companion object {
        fun fromAmPm(s: String) = TimeQu().apply {
            val x = s.split(" ")[0]

            t = s.split(" ")[1]
            h = x.split(":")[0].toInt().toString()
            m = x.split(":")[1].toInt().toString()
        }
    }

    private var h = ""
    private var m = ""
    private var t = ""

    fun to24(): String {
        val newH = if (t.equals("am", true)) h else "${h.toInt() + 12}"
        return "${newH.padStart(2, '0')}:${m.padStart(2, '0')}"
    }

    fun toHour() = if (t.equals("am", true)) h else "${h.toInt() + 12}"

    fun toMinute() = m
}