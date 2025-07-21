package com.fauzan.zainullah.bookingkonsultasiapp.data.remote

import com.fauzan.zainullah.bookingkonsultasiapp.data.model.AddBookingRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.Booking
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.Dosen
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.GenericResponse
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.LoginRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.RegisterRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<GenericResponse<User>>
    @POST("register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<GenericResponse<Any>>
    @GET("dosen")
    suspend fun getAllDosen(): Response<GenericResponse<List<Dosen>>>
    @GET("booking")
    suspend fun getAllBookings(): Response<GenericResponse<List<Booking>>>
    @POST("booking")
    suspend fun createBooking(
        @Body addBookingRequest: AddBookingRequest
    ): Response<GenericResponse<Any>>
}
