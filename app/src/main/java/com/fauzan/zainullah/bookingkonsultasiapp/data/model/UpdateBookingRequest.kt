package com.fauzan.zainullah.bookingkonsultasiapp.data.model

import com.google.gson.annotations.SerializedName

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
