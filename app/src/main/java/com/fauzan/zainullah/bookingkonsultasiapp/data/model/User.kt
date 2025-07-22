package com.fauzan.zainullah.bookingkonsultasiapp.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nama")
    val nama: String,
    @SerializedName("nim")
    val nim: String?,
    @SerializedName("role")
    val role: String,
    @SerializedName("username")
    val username: String
)