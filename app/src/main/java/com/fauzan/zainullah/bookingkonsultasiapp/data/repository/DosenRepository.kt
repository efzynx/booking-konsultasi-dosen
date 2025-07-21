// file: data/repository/DosenRepository.kt
package com.fauzan.zainullah.bookingkonsultasiapp.data.repository

import com.fauzan.zainullah.bookingkonsultasiapp.data.remote.RetrofitClient

class DosenRepository {
    private val apiService = RetrofitClient.instance
    suspend fun getAllDosen() = apiService.getAllDosen()
}