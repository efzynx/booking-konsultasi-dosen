package com.fauzan.zainullah.bookingkonsultasiapp.data.repository

import com.fauzan.zainullah.bookingkonsultasiapp.data.model.LoginRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.RegisterRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.remote.RetrofitClient

class AuthRepository {
    private val apiservice = RetrofitClient.instance
    suspend fun login(loginRequest: LoginRequest) = apiservice.login(loginRequest)
    suspend fun register(registerRequest: RegisterRequest) = apiservice.register(registerRequest)
}