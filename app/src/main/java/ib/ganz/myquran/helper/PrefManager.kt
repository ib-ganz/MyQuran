package ib.ganz.myquran.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

object PrefManager {

    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private const val lastReadPosition = "lastReadPosition"
    private const val lastReadAutoSave = "lastReadAutoSave"

    @SuppressLint("CommitPrefEdits")
    fun init(f: () -> Context) {
        sp = f().getSharedPreferences("ib.ganz.myquran", Context.MODE_PRIVATE)
        editor = sp.edit()
    }

    fun getLastReadPosition(): Int {
        return sp.getInt(lastReadPosition, 0)
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
}