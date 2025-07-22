package com.fauzan.zainullah.bookingkonsultasiapp.data.remote

import com.fauzan.zainullah.bookingkonsultasiapp.data.model.AddBookingRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.Booking
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.Dosen
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.GenericResponse
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.LoginRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.LoginResponse
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.RegisterRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.UpdateBookingRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.UpdateProfileRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.UpdateStatusRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<GenericResponse<LoginResponse>>

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

    @GET("profile")
    suspend fun getProfile(): Response<GenericResponse<User>>

    @PUT("profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<GenericResponse<Any>>

    @DELETE("profile")
    suspend fun deleteProfile(): Response<GenericResponse<Any>>

    @PUT("booking/{id}")
    suspend fun updateBooking(
        @Path("id") bookingId: Int,
        @Body request: UpdateStatusRequest
    ): Response<GenericResponse<Booking>>

    @PUT("booking/{id}")
    suspend fun updateBookingDetails(
        @Path("id") bookingId: Int,
        @Body request: UpdateBookingRequest // Menggunakan model request yang baru
    ): Response<GenericResponse<Booking>>

    @DELETE("booking/{id}")
    suspend fun deleteBooking(
        @Path("id") bookingId: Int
    ): Response<GenericResponse<Any>>
}
