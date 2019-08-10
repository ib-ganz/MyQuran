package ib.ganz.myquran.customclass

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.widget.ImageViewCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import com.google.gson.annotations.SerializedName
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import ib.ganz.myquran.R
import ib.ganz.myquran.kotlinstuff.*
import ib.ganz.myquran.network.ApiClient
import ib.ganz.myquran.network.GxonConverterFaxtory
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_jadwal.view.*
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.lang.Exception

class JadwalView : LinearLayout {

    companion object {

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("http://muslimsalat.com/")
                .client(ApiClient.okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GxonConverterFaxtory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }

        interface IJadwalService {

            @GET("{city}.json")
            fun getJadwal(
                @Path("city") city: String,
                @Query("key") key: String
            ): Single<JadwalData>

        }

        private val service: IJadwalService by lazy { retrofit.create(IJadwalService::class.java) }

        private fun srv(city: String) = service.getJadwal(city, "f3f2363843f5863f5d02b98d16b9a110")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private lateinit var cd: CompositeDisposable
    private var attemptCount = 0
    private var onFinished: ((JadwalItem) -> Unit)? = null

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_jadwal, this, true)
        orientation = HORIZONTAL
    }

    fun compositeDisposable(cd: CompositeDisposable) {
        this.cd = cd
    }

    fun onFinished(f: (JadwalItem) -> Unit) {
        onFinished = f
    }

    fun start() {
        attemptCount++
        cd.add(srv("Yogyakarta").subscribe({

            val j = it.items[0]
            j.run {
                tJamSubuh.text = fajr.fromAmPm().to24()
                tJamDzuhur.text = dhuhr.fromAmPm().to24()
                tJamAshar.text = asr.fromAmPm().to24()
                tJamMagrib.text = maghrib.fromAmPm().to24()
                tJamIsya.text = isha.fromAmPm().to24()
            }
            setJadwalBackgroundColor(j)

        }, {
            if (attemptCount < 5) {
                start()
            }
        }))
    }

    private fun setJadwalBackgroundColor(j: JadwalItem) {

        val now = DateQu.now().toCalendar()

        fun isSubuh(): Boolean {
            val start = DateQu.now().toCalendar().apply {
                setHour(j.fajr.fromAmPm().toHour().toInt())
                setMinute(j.fajr.fromAmPm().toMinute().toInt())
            }
            val end = DateQu.now().toCalendar().apply {
                setHour(j.shurooq.fromAmPm().toHour().toInt())
                setMinute(j.shurooq.fromAmPm().toMinute().toInt())
            }
            return now.before(end) && now.after(start)
        }

        fun isDzuhur(): Boolean {
            val start = DateQu.now().toCalendar().apply {
                setHour(j.dhuhr.fromAmPm().toHour().toInt())
                setMinute(j.dhuhr.fromAmPm().toMinute().toInt())
            }
            val end = DateQu.now().toCalendar().apply {
                setHour(j.asr.fromAmPm().toHour().toInt())
                setMinute(j.asr.fromAmPm().toMinute().toInt())
            }
            return now.before(end) && now.after(start)
        }

        fun isAshar(): Boolean {
            val start = DateQu.now().toCalendar().apply {
                setHour(j.asr.fromAmPm().toHour().toInt())
                setMinute(j.asr.fromAmPm().toMinute().toInt())
            }
            val end = DateQu.now().toCalendar().apply {
                setHour(j.maghrib.fromAmPm().toHour().toInt())
                setMinute(j.maghrib.fromAmPm().toMinute().toInt())
            }
            return now.before(end) && now.after(start)
        }

        fun isMaghrib(): Boolean {
            val start = DateQu.now().toCalendar().apply {
                setHour(j.maghrib.fromAmPm().toHour().toInt())
                setMinute(j.maghrib.fromAmPm().toMinute().toInt())
            }
            val end = DateQu.now().toCalendar().apply {
                setHour(j.isha.fromAmPm().toHour().toInt())
                setMinute(j.isha.fromAmPm().toMinute().toInt())
            }
            return now.before(end) && now.after(start)
        }

        fun isIsya(): Boolean {
            val start = DateQu.now().toCalendar().apply {
                setHour(j.isha.fromAmPm().toHour().toInt())
                setMinute(j.isha.fromAmPm().toMinute().toInt())
            }
            val end = DateQu.now().toCalendar().apply {
                setHour(j.shurooq.fromAmPm().toHour().toInt())
                setMinute(j.shurooq.fromAmPm().toMinute().toInt())
            }
            return now.before(end) && now.after(start)
        }

        val bg = arrayOf(bgSubuh, bgDzuhur, bgAshar, bgMagrib, bgIsya)
        val i = arrayOf(iSubuh, iDzuhur, iAshar, iMagrib, iIsya)
        val l = arrayOf(lSubuh, lDzuhur, lAshar, lMagrib, lIsya)
        val t = arrayOf(tSubuh, tDzuhur, tAshar, tMagrib, tIsya)
        val tj = arrayOf(tJamSubuh, tJamDzuhur, tJamAshar, tJamMagrib, tJamIsya)
        val sel = when {
            isSubuh() -> 0
            isDzuhur() -> 1
            isAshar() -> 2
            isMaghrib() -> 3
            isIsya() -> 4
            else -> 4
        }

        Thread(Runnable {
            (context as Activity).runOnUiThread {
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
        }).start()

        onFinished?.invoke(j)
    }
}