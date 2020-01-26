package ib.ganz.myquran.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.*
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import ib.ganz.myquran.R
import ib.ganz.myquran.kotlinstuff.extfun.requestLocationPermission
import kotlinx.android.synthetic.main.activity_compass.*


class CompassActivity : BaseActivity() {

    companion object {
        fun go(c: Context) = c.startActivity(Intent(c, CompassActivity::class.java))
    }

    private var currentDegree = 0f
    private var currentDegreeNeedle = 0f
    private lateinit var mSensorManager: SensorManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var curLoc: Location

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass)

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onDestroy() {
        mSensorManager.unregisterListener(sensorEventListener)
        super.onDestroy()
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission {  }
        }
        else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    curLoc = it
                    calculateQibla()
                }
            }
        }
    }

    private fun calculateQibla() {
        val userLoc = Location("service Provider")
        userLoc.latitude = curLoc.latitude
        userLoc.longitude = curLoc.longitude
        userLoc.altitude = curLoc.altitude

        val destinationLoc = Location("service Provider")
        destinationLoc.latitude = 21.422487
        destinationLoc.longitude = 39.826206

        var bearTo = userLoc.bearingTo(destinationLoc)
        if (bearTo < 0) {
            bearTo += 360
        }
    }

    private val sensorEventListener = object : SensorEventListener {
        @SuppressLint("SetTextI18n")
        override fun onSensorChanged(event: SensorEvent?) {
            if (event == null) return
            if (!::curLoc.isInitialized) return

//            val degree = Math.round(event.values[0]).toFloat()
//            tvHeading.text = "Heading: $degree degrees"
//            val ra = RotateAnimation(
//                currentDegree,
//                -degree,
//                Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF,
//                0.5f
//            )
//            ra.duration = 210
//            ra.fillAfter = true
//            imageViewCompass.startAnimation(ra)
//            currentDegree = -degree

            // #######################

            val degree = Math.round(event.values[0]).toFloat()
            var head = Math.round(event.values[0]).toFloat()
            var bearTo: Float
            val destinationLoc = Location("service Provider").apply {
                latitude = 21.422487
                longitude = 39.826206
            }

            bearTo = curLoc.bearingTo(destinationLoc)

            val geoField = GeomagneticField(
                curLoc.latitude.toFloat(),
                curLoc.longitude.toFloat(),
                curLoc.altitude.toFloat(),
                System.currentTimeMillis()
            )
            head -= geoField.declination

            if (bearTo < 0) {
                bearTo += 360
            }

            var direction = bearTo - head

            if (direction < 0) {
                direction += 360
            }
            tvHeading.text = "$degree°"

            val raQibla = RotateAnimation(
                currentDegreeNeedle,
                direction,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            raQibla.duration = 210
            raQibla.fillAfter = true

            iQibla.startAnimation(raQibla)

            currentDegreeNeedle = direction
            val ra = RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            ra.duration = 210
            ra.fillAfter = true
            imageViewCompass.startAnimation(ra)

            currentDegree = -degree
            onDegreeChange(degree, direction)
        }
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) { }
    }

    private var curHeading = -1
    private fun onDegreeChange(degree: Float, qiblaDegree: Float) {
        if (degree in 357..360 || degree in 0..3) {
            if (curHeading != 0) {
                speak { "anda menghadap ke utara" }
            }
            curHeading = 0
        }
        else if (degree in 87..93) {
            if (curHeading != 1) {
                speak { "anda menghadap ke timur" }
            }
            curHeading = 1
        }
        else if (degree in 177..183) {
            if (curHeading != 2) {
                speak { "anda menghadap ke selatan" }
            }
            curHeading = 2
        }
        else if (degree in 267..273) {
            if (curHeading != 3) {
                speak { "anda menghadap ke barat" }
            }
            curHeading = 3
        }
        else if (qiblaDegree in 357..360 || qiblaDegree in 0..3) {
            if (curHeading != 4) {
                speak { "anda menghadap ke kiblat" }
            }
            curHeading = 4
        }
        else {
            curHeading = -1
        }
    }
}
