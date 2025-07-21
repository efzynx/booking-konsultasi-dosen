// file: utils/SessionManager.kt
package com.fauzan.zainullah.bookingkonsultasiapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.User
import com.google.gson.Gson

object SessionManager {

    private const val PREFS_NAME = "BookingAppPrefs"
    private const val KEY_IS_LOGGED_IN = "isLoggedIn"
    private const val KEY_USER_DATA = "userData"

    private lateinit var sharedPreferences: SharedPreferences

    // Fungsi ini harus dipanggil sekali di awal aplikasi
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveSession(isLoggedIn: Boolean, user: User?) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)

        // Simpan data user sebagai JSON string
        if (user != null) {
            val userJson = Gson().toJson(user)
            editor.putString(KEY_USER_DATA, userJson)
        } else {
            editor.remove(KEY_USER_DATA)
        }
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserData(): User? {
        val userJson = sharedPreferences.getString(KEY_USER_DATA, null)
        return if (userJson != null) {
            Gson().fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    fun clearSession() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
