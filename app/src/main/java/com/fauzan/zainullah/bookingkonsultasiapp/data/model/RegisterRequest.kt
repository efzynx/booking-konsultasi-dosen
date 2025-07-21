// file: data/model/RegisterRequest.kt
package com.fauzan.zainullah.bookingkonsultasiapp.data.model

// Model untuk data yang DIKIRIM saat registrasi
data class RegisterRequest(
    val nama: String,
    val nim: String,
    val username: String,
    val password: String
)