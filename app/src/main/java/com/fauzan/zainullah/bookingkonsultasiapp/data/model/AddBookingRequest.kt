// file: data/model/AddBookingRequest.kt
package com.fauzan.zainullah.bookingkonsultasiapp.data.model

import com.google.gson.annotations.SerializedName

// Model untuk data yang DIKIRIM saat membuat booking baru
data class AddBookingRequest(
    @SerializedName("nama_mahasiswa")
    val namaMahasiswa: String,
    @SerializedName("nim")
    val nim: String,
    @SerializedName("dosen_id")
    val dosenId: Int,
    @SerializedName("tanggal")
    val tanggal: String, // Format: "YYYY-MM-DD"
    @SerializedName("jam")
    val jam: String, // Format: "HH:MM:SS"
    @SerializedName("topik_konsultasi")
    val topikKonsultasi: String
)