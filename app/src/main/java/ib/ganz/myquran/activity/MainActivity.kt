package ib.ganz.myquran.activity

import android.os.Bundle
import ib.ganz.myquran.R
import ib.ganz.myquran.customclass.BackgroundManager
import ib.ganz.myquran.kotlinstuff.click
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bSetting.click {  }
        bCompass.click {  }

        jadwalView.run {
            compositeDisposable(compositeDisposable)
            onFinished { BackgroundManager.init(iBg, it) }
            start()
        }
    }
}
