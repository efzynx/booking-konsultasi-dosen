package com.fauzan.zainullah.bookingkonsultasiapp.data.model

import com.google.gson.annotations.SerializedName

data class Dosen(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nama_dosen")
    val namaDosen: String,
    @SerializedName("mata_kuliah")
    val mataKuliah: String,
    @SerializedName("user_id")
    val userId: Int
)