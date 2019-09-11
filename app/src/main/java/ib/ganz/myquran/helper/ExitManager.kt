package ib.ganz.emaklon.helper

import android.app.Activity
import android.os.Handler
import android.widget.Toast

object ExitManager {
    private var doubleBackToExitPressedOnce = false

    fun go(a: Activity) {
        if (doubleBackToExitPressedOnce) {
            a.finish()
            return
        }

        doubleBackToExitPressedOnce = true
        Toast.makeText(a, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}
