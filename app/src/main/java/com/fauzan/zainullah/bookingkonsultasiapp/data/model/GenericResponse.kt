package com.fauzan.zainullah.bookingkonsultasiapp.data.model

data class GenericResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)