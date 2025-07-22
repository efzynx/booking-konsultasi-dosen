// file: utils/SessionManager.kt
package com.fauzan.zainullah.bookingkonsultasiapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.User
import com.google.gson.Gson

object SessionManager {

    private const val PREFS_NAME = "BookingAppPrefs"
    private const val KEY_AUTH_TOKEN = "authToken"
    private const val KEY_USER_DATA = "userData"

    private lateinit var sharedPreferences: SharedPreferences

    // Fungsi ini harus dipanggil sekali di awal aplikasi, misalnya di LoginActivity
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Fungsi untuk menyimpan token dan data user setelah login berhasil
    fun saveAuth(token: String, user: User) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_AUTH_TOKEN, token)
        val userJson = Gson().toJson(user)
        editor.putString(KEY_USER_DATA, userJson)
        editor.apply()
    }

    // Fungsi untuk mengambil token (yang dibutuhkan oleh AuthInterceptor)
    fun getToken(): String? {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    }

    // Fungsi untuk mengambil data user yang sedang login
    fun getUserData(): User? {
        val userJson = sharedPreferences.getString(KEY_USER_DATA, null)
        return if (userJson != null) {
            Gson().fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    // Fungsi untuk menghapus semua sesi saat logout
    fun clearSession() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
