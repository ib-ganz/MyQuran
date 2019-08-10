package ib.ganz.myquran.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.internal.observers.ConsumerSingleObserver

/**
 * Created by limakali on 3/17/2018.
 */

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    protected lateinit var progressDialog: ProgressDialog
    protected lateinit var compositeDisposable: CompositeDisposable
    public lateinit var fm: FragmentManager
    protected lateinit var a: BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(this)
        compositeDisposable = CompositeDisposable()
        fm = supportFragmentManager
        a = this
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!compositeDisposable.isDisposed) compositeDisposable.dispose()
    }

    inline fun <reified T> load(s: Single<T>, noinline block: (T) -> Unit) {
        when (T::class) {
            List::class -> {
                cd().add(s.subscribe({
                    onSuccess(it as List<*>)
                    block(it)
                }, ::handleError))
            }
            else -> {
                lcd().add(s.subscribe({
                    onSuccess()
                    block(it)
                }, ::handleError))
            }
        }
    }

    fun ocd(): CompositeDisposable {
        return compositeDisposable
    }

    fun cd(): CompositeDisposable {

        return compositeDisposable
    }

    fun lcd(): CompositeDisposable {
        showLoading()
        return compositeDisposable
    }

    fun toast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    fun toastKoneksi() {
        toast("Periksa koneksi Anda!")
    }

    fun onSuccess() {
        hideLoading()
    }

    fun onSuccess(b: List<*>) // otomatis show no data if r is empty
    {
        onSuccess()
    }

    fun onSuccess(toast: String) // otomatis toast
    {
        onSuccess()
        toast(toast)
    }

    fun handleError(e: Throwable) {
        hideLoading()
        toastKoneksi()
    }

    fun toggleVisibility(b: Boolean, vararg vs: View) {
        if (b)
            show(*vs)
        else
            hide(*vs)
    }

    fun show(b: Boolean, vararg vs: View) {
        if (b) show(*vs)
    }

    fun show(vararg vs: View?) {
        for (v in vs) {
            if (v != null && v.visibility != View.VISIBLE) v.visibility = View.VISIBLE
        }
    }

    fun hide(b: Boolean, vararg vs: View) {
        if (b) hide(*vs)
    }

    fun hide(vararg vs: View?) {
        for (v in vs) {
            if (v != null && v.visibility != View.GONE) v.visibility = View.GONE
        }
    }

    fun showLoading() {
        if (!progressDialog.isShowing) progressDialog.show()
    }

    fun hideLoading() {
        if (progressDialog.isShowing) progressDialog.dismiss()
    }

    fun isEmpty(t: TextView): Boolean {
        return txt(t).isEmpty()
    }

    fun txt(e: TextView): String {
        return e.text.toString().trim { it <= ' ' }
    }

    fun check(vararg edts: EditText): Boolean {
        for (e in edts) {
            e.error = null
        }

        for (e in edts) {
            if (e.text.toString().trim { it <= ' ' }.isEmpty()) {
                e.requestFocus()
                e.error = "Isi semua field!"
                return false
            }
        }
        return true
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    fun <T> Single<T>.subs(block: (T) -> Unit): Disposable {
        val onSuccess: Consumer<in T> = Consumer { block(it) }
        val onError: Consumer<in Throwable> = Consumer { handleError(it) }
        val s = ConsumerSingleObserver<T>(onSuccess, onError)
        subscribe(s)
        return s
    }
}
