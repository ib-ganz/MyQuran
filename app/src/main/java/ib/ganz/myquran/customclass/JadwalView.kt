package ib.ganz.myquran.customclass

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.location.Geocoder
import android.os.Build
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.ImageViewCompat
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import ib.ganz.myquran.R
import ib.ganz.myquran.kotlinstuff.*
import ib.ganz.myquran.kotlinstuff.extfun.requestLocationPermission
import ib.ganz.myquran.network.ApiClient
import ib.ganz.myquran.network.GxonConverterFaxtory
import kotlinx.android.synthetic.main.view_jadwal.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class JadwalView : LinearLayout {

    companion object {

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("http://muslimsalat.com/")
                .client(ApiClient.okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GxonConverterFaxtory.create())
                .build()
        }

        interface IJadwalService {

            @GET("{city}.json")
            suspend fun getJadwal(
                @Path("city") city: String,
                @Query("key") key: String
            ): JadwalData

        }

        private val service: IJadwalService by lazy { retrofit.create(IJadwalService::class.java) }
    }

    private var cs: CoroutineScope? = null
    private var attemptCount = 0
    private var onFinished: ((JadwalItem) -> Unit)? = null
    private var speaker: ((String) -> Unit)? = null
    private var vibrator: (() -> Unit)? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient
    private val apiKey = "f3f2363843f5863f5d02b98d16b9a110"

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_jadwal, this, true)
        orientation = HORIZONTAL
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    }

    fun onFinished(f: (JadwalItem) -> Unit) {
        onFinished = f
    }

    fun speaker(f: () -> ((String) -> Unit)) {
        speaker = f()
    }

    fun vibrator(f: () -> (() -> Unit)) {
        vibrator = f()
    }

    fun scope(s: () -> CoroutineScope) {
        this.cs = s()
    }

    fun start() {
        run()
        Handler().postDelayed(::run, 1000 * 10)
    }

    private fun run() {
        Log.d("kompas", "running")
        attemptCount++
        try {
            cs?.launch {
                val city = getCity() ?: "yogyakarta"
                val jadwalData = withContext(Dispatchers.IO) { service.getJadwal(city, apiKey) }
                val jadwalItem = jadwalData.items[0]
                val selectedTime = getSelectedTime(jadwalItem)

                setTimeText(city, jadwalItem)
                setJadwalBackgroundColor(selectedTime)
                setWhatz(selectedTime, jadwalItem)
                progressBar.beGone()
            }
        }
        catch (e: Exception) {
            if (attemptCount < 5) {
                start()
            }
        }
    }

    private suspend fun getCity() = suspendCoroutine<String?> { x ->
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            (context as Activity).requestLocationPermission {  }
        }
        else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    val cityName = addresses[0].getAddressLine(0).toLowerCase().split("kabupaten")[1].split(",")[0].trim()
                    x.resume(cityName)
                }
                else {
                    x.resume(null)
                }
            }
        }
    }

    private fun setTimeText(city: String, j: JadwalItem) = j.run {
        tCity.text = city.capitalize()
        tJamSubuh.text = fajr.fromAmPm().to24()
        tJamDzuhur.text = dhuhr.fromAmPm().to24()
        tJamAshar.text = asr.fromAmPm().to24()
        tJamMagrib.text = maghrib.fromAmPm().to24()
        tJamIsya.text = isha.fromAmPm().to24()
    }

    private fun getSelectedTime(j: JadwalItem): Int {
        val now = now().toCalendar().apply { setSecond(59) }
        Log.d("kompas", "isyaStart: " + j.isyaStart().timeInMillis)
        Log.d("kompas", "isyaEnd: " + j.isyaEnd().timeInMillis)
        Log.d("kompas", "now: " + now.timeInMillis)
        return when {
            now.before(j.subuhEnd()) && now.after(j.subuhStart()) -> 0
            now.before(j.dzuhurEnd()) && now.after(j.dzuhurStart()) -> 1
            now.before(j.asharEnd()) && now.after(j.asharStart()) -> 2
            now.before(j.maghribEnd()) && now.after(j.maghribStart()) -> 3
            now.before(j.isyaEnd()) && now.after(j.isyaStart()) -> 4
            else -> 5
        }
    }

    private fun setJadwalBackgroundColor(sel: Int) {
        if (sel == 5) return

        val bg = arrayOf(bgSubuh, bgDzuhur, bgAshar, bgMagrib, bgIsya)
        val i = arrayOf(iSubuh, iDzuhur, iAshar, iMagrib, iIsya)
        val l = arrayOf(lSubuh, lDzuhur, lAshar, lMagrib, lIsya)
        val t = arrayOf(tSubuh, tDzuhur, tAshar, tMagrib, tIsya)
        val tj = arrayOf(tJamSubuh, tJamDzuhur, tJamAshar, tJamMagrib, tJamIsya)

        val selectedBg = ContextCompat.getDrawable(context, R.drawable.bg_jadwal_rounded_selected)
        val unselectedBg = ContextCompat.getDrawable(context, R.drawable.bg_jadwal_rounded)
        fun getBg(b: Boolean) = if (b) selectedBg else unselectedBg
        val selectedColor = ContextCompat.getColor(context, R.color.colorWhite)
        val unselectedColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        fun getColor(b: Boolean) = if (b) selectedColor else unselectedColor

        for (q in 0..4) {
            bg[q].background = getBg(q == sel)
            ImageViewCompat.setImageTintList(i[q], ColorStateList.valueOf(getColor(q == sel)))
            l[q].setBackgroundColor(getColor(q == sel))
            t[q].setTextColor(getColor(q == sel))
            tj[q].setTextColor(getColor(q == sel))
        }
    }

    private fun setWhatz(sel: Int, j: JadwalItem) {
        val now = now().toCalendar().timeInMillis
        val thirtyMinute = 1000 * 60 * 30
        val oneHour = 1000 * 60 * 60
        var what = ""
        when (sel) {
            0 -> {
                val diff = j.subuhEnd().timeInMillis - now
                val inMin = TimeUnit.MILLISECONDS.toMinutes(diff)
                what = if (diff < thirtyMinute) {
                    "waktu subuh akan berakhir dalam $inMin menit lagi"
                } else {
                    "sekarang memasuki waktu subuh"
                }
            }
            1 -> {
                val diff = j.dzuhurEnd().timeInMillis - now
                val inMin = TimeUnit.MILLISECONDS.toMinutes(diff)
                what = if (diff < thirtyMinute) {
                    "waktu dzuhur akan berakhir dalam $inMin menit lagi"
                } else {
                    "sekarang memasuki waktu dzuhur"
                }
            }
            2 -> {
                val diff = j.asharEnd().timeInMillis - now
                val inMin = TimeUnit.MILLISECONDS.toMinutes(diff)
                what = if (diff < thirtyMinute) {
                    "waktu ashar akan berakhir dalam $inMin menit lagi"
                } else {
                    "sekarang memasuki waktu ashar"
                }
            }
            3 -> {
                val diff = j.maghribEnd().timeInMillis - now
                val inMin = TimeUnit.MILLISECONDS.toMinutes(diff)
                what = if (diff < thirtyMinute) {
                    "waktu maghrib akan berakhir dalam $inMin menit lagi"
                } else {
                    "sekarang memasuki waktu maghrib"
                }
            }
            4 -> {
                val diff = j.isyaEnd().timeInMillis - now
                val inMin = TimeUnit.MILLISECONDS.toMinutes(diff)
                what = if (diff < thirtyMinute) {
                    "waktu isya akan berakhir dalam $inMin menit lagi"
                } else {
                    "sekarang memasuki waktu isya"
                }
            }
            5 -> {
                val diff = now - j.subuhEnd().timeInMillis
                val inMin = TimeUnit.MILLISECONDS.toMinutes(diff)

                val diff2 = j.dzuhurStart().timeInMillis - now
                val inMin2 = TimeUnit.MILLISECONDS.toMinutes(diff2)

                what = when {
                    diff < oneHour -> "waktu subuh telah berakhir pada $inMin menit yang lalu"
                    diff2 < oneHour -> "waktu dzuhur akan masuk dalam $inMin2 menit lagi"
                    else -> "sekarang memasuki waktu duha"
                }
            }
        }
        tWhat.text = what
        rootJadwal.click {
            vibrator?.invoke()
            speaker?.invoke(what)
        }
    }
}