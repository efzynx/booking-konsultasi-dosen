// file: data/model/LoginResponse.kt
package com.fauzan.zainullah.bookingkonsultasiapp.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("user")
    val user: User,
    @SerializedName("access_token")
    val accessToken: String
)
