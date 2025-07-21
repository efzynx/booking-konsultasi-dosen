package com.fauzan.zainullah.bookingkonsultasiapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.Booking
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.GenericResponse
import com.fauzan.zainullah.bookingkonsultasiapp.data.repository.BookingRepository
import com.fauzan.zainullah.bookingkonsultasiapp.utils.Resource
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = BookingRepository()

    private val _bookingResult = MutableLiveData<Resource<GenericResponse<List<Booking>>>>()
    val bookingResult: LiveData<Resource<GenericResponse<List<Booking>>>> = _bookingResult

    fun fetchBookings() {
        viewModelScope.launch {
            _bookingResult.postValue(Resource.Loading())
            try {
                val response = repository.getAllBookings()
                if (response.isSuccessful && response.body() != null) {
                    _bookingResult.postValue(Resource.Success(response.body()!!))
                } else {
                    _bookingResult.postValue(Resource.Error("Gagal mengambil data booking."))
                }
            } catch (e: Exception) {
                _bookingResult.postValue(Resource.Error("Terjadi kesalahan jaringan: ${e.message}"))
            }
        }
    }
}