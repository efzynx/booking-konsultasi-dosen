package com.fauzan.zainullah.bookingkonsultasiapp.data.repository

import com.fauzan.zainullah.bookingkonsultasiapp.data.model.AddBookingRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.remote.RetrofitClient

class BookingRepository {
    private val apiService = RetrofitClient.instance

    suspend fun getAllBookings() = apiService.getAllBookings()
    suspend fun createBooking(addBookingRequest: AddBookingRequest) =
        apiService.createBooking(addBookingRequest)

    // Nanti kita akan tambahkan fungsi lain seperti createBooking, dll di sini
}
