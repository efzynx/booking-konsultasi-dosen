package com.fauzan.zainullah.bookingkonsultasiapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Model data untuk mengirim permintaan update booking dari mahasiswa.
 * Berisi semua field yang bisa diubah oleh mahasiswa.
 */
data class UpdateBookingRequest(
    @SerializedName("dosen_id")
    val dosenId: Int,

    @SerializedName("tanggal")
    val tanggal: String,

    @SerializedName("jam")
    val jam: String,

    @SerializedName("topik_konsultasi")
    val topikKonsultasi: String
)
