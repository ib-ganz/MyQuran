package ib.ganz.myquran.activity

import android.os.Bundle
import android.os.Handler
import ib.ganz.myquran.R
import ib.ganz.myquran.kotlinstuff.beFullScreen

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        beFullScreen()

        Handler().postDelayed({ MainActivity.go(this) }, 1000)
    }
}
