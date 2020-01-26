package ib.ganz.myquran.manager

import androidx.multidex.MultiDexApplication

class AppManager: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        PrefManager.init { this }
    }
}