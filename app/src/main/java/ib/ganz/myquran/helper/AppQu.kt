package ib.ganz.myquran.helper

import android.app.Application

class AppQu: Application() {
    override fun onCreate() {
        super.onCreate()
        PrefManager.init { this }
    }
}