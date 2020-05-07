package ib.ganz.myquran.manager

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

object PrefManager {

    const val MODE_1 = 0 // Suara dan getar -> aksi
    const val MODE_2 = 1 // Suara -> aksi
    const val MODE_3 = 2 // Getar -> aksi
    const val MODE_4 = 3 // -> suara, getar dan aksi
    const val MODE_5 = 4 // -> suara dan aksi
    const val MODE_6 = 5 // -> getar dan aksi
    const val MODE_7 = 6 // -> aksi

    val QARIS = arrayOf(
        "AbdulBaset/Murattal",
        "Alafasy",
        "Minshawi/Murattal",
        "Rifai",
        "Shatri",
        "Shuraym",
        "Sudais"
    )

    private const val defaultTombol = MODE_1
    private const val defaultQariPos = 0
    private const val defaultQuranSize = 24.0F
    private const val defaultTerjemahSize = 16.0F
    private const val defaultTafsirSize = 16.0F

    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private const val skipTTSLang = "skipttslang"
    private const val qari = "qari"
    private const val lastReadPosition = "lastReadPosition"
    private const val lastReadAutoSave = "lastReadAutoSave"
    private const val buttonMode = "buttonMode"
    private const val suara = "suara"
    private const val quranSize = "quranSize"
    private const val terjemahSize = "terjemahSize"
    private const val tafsirSize = "tafsirSize"

    @SuppressLint("CommitPrefEdits")
    fun init(f: () -> Context) {
        sp = f().getSharedPreferences("ib.ganz.myquran", Context.MODE_PRIVATE)
        editor = sp.edit()
    }

    fun reset() {
        setButtonMode(defaultTombol)
        setSuara(true)
        setQariPos(defaultQariPos)
        setQuranSize(defaultQuranSize)
        setTerjemahSize(defaultTerjemahSize)
        setTafsirSize(defaultTafsirSize)
    }

    fun getSkipTTSLang(): Boolean {
        return sp.getBoolean(skipTTSLang, false)
    }

    fun setSkipTTSLang(i: Boolean) {
        editor.putBoolean(skipTTSLang, i).commit()
    }

    fun getLastReadPosition(): Int {
        return sp.getInt(lastReadPosition, 1)
    }

    fun setLastReadPosition(i: Int) {
        editor.putInt(lastReadPosition, i).commit()
    }

    fun isLastReadAutoSave(): Boolean {
        return sp.getBoolean(lastReadAutoSave, true)
    }

    fun setIsLastReadAutoSave(b: Boolean) {
        editor.putBoolean(lastReadAutoSave, b).commit()
    }

    fun getButtonMode(): Int {
        return sp.getInt(buttonMode, defaultTombol)
    }

    fun setButtonMode(i: Int) {
        editor.putInt(buttonMode, i).commit()
    }

    fun getSuara(): Boolean {
        return sp.getBoolean(suara, true)
    }

    fun setSuara(b: Boolean) {
        editor.putBoolean(suara, b).commit()
    }

    fun getQariPos(): Int {
        return sp.getInt(qari, 0)
    }

    fun getQari(): String {
        return QARIS[sp.getInt(qari, defaultQariPos)]
    }

    fun setQariPos(i: Int) {
        editor.putInt(qari, i).commit()
    }

    fun getQuranSize(): Float {
        return sp.getFloat(quranSize, defaultQuranSize)
    }

    fun setQuranSize(i: Float) {
        editor.putFloat(quranSize, i).commit()
    }

    fun getTerjemahSize(): Float {
        return sp.getFloat(terjemahSize, defaultTerjemahSize)
    }

    fun setTerjemahSize(i: Float) {
        editor.putFloat(terjemahSize, i).commit()
    }

    fun getTafsirSize(): Float {
        return sp.getFloat(tafsirSize, defaultTafsirSize)
    }

    fun setTafsirSize(i: Float) {
        editor.putFloat(tafsirSize, i).commit()
    }
}