package com.fauzan.zainullah.bookingkonsultasiapp.data.repository

import com.fauzan.zainullah.bookingkonsultasiapp.data.model.UpdateProfileRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.remote.RetrofitClient

class ProfileRepository {
    private val apiService = RetrofitClient.instance
    suspend fun getProfile() = apiService.getProfile()
    suspend fun updateProfile(request: UpdateProfileRequest) = apiService.updateProfile(request)
    suspend fun deleteProfile() = apiService.deleteProfile()
}