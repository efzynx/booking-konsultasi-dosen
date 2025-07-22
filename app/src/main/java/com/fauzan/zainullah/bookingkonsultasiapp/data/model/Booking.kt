package com.fauzan.zainullah.bookingkonsultasiapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Booking(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nama_mahasiswa")
    val namaMahasiswa: String,
    @SerializedName("nim")
    val nim: String,
    @SerializedName("dosen_id")
    val dosenId: Int,
    @SerializedName("dosen_info")
    val dosenInfo: Dosen,
    @SerializedName("tanggal")
    val tanggal: String,
    @SerializedName("jam")
    val jam: String,
    @SerializedName("topik_konsultasi")
    val topikKonsultasi: String,
    @SerializedName("status")
    val status: String
) : Parcelable