package ib.ganz.myquran.manager

import android.content.Context
import androidx.multidex.MultiDexApplication
import ib.ganz.myquran.helper.NewLocaleHelper

class AppManager: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        PrefManager.init { this }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(NewLocaleHelper.onAttach(base))
    }
}