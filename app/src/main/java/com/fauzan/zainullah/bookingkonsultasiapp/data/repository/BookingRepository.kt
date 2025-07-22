package com.fauzan.zainullah.bookingkonsultasiapp.data.repository

import com.fauzan.zainullah.bookingkonsultasiapp.data.model.AddBookingRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.Booking
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.GenericResponse
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.UpdateBookingRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.UpdateStatusRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.remote.RetrofitClient
import retrofit2.Response

class BookingRepository {
    private val apiService = RetrofitClient.instance

    suspend fun getAllBookings() = apiService.getAllBookings()
    suspend fun createBooking(addBookingRequest: AddBookingRequest) =
        apiService.createBooking(addBookingRequest)
    suspend fun updateBookingStatus(bookingId: Int, status: String): Response<GenericResponse<Booking>> {
        val request = UpdateStatusRequest(status = status)
        return RetrofitClient.instance.updateBooking(bookingId, request)
    }
    suspend fun updateBookingDetails(
        bookingId: Int,
        dosenId: Int,
        tanggal: String,
        jam: String,
        topik: String
    ): Response<GenericResponse<Booking>> {
        val request = UpdateBookingRequest(
            dosenId = dosenId,
            tanggal = tanggal,
            jam = jam,
            topikKonsultasi = topik
        )
        return apiService.updateBookingDetails(bookingId, request)
    }
    suspend fun deleteBooking(bookingId: Int): Response<GenericResponse<Any>> {
        return apiService.deleteBooking(bookingId)
    }
}
