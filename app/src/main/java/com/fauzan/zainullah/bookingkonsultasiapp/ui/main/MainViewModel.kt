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

    // LiveData untuk daftar booking (State)
    private val _bookingResult = MutableLiveData<Resource<GenericResponse<List<Booking>>>>()
    val bookingResult: LiveData<Resource<GenericResponse<List<Booking>>>> = _bookingResult

    // LiveData untuk hasil update status (Event)
    private val _updateResult = MutableLiveData<Resource<GenericResponse<Booking>>?>()
    val updateResult: LiveData<Resource<GenericResponse<Booking>>?> = _updateResult

    // LiveData untuk hasil hapus booking (Event)
    private val _deleteResult = MutableLiveData<Resource<GenericResponse<Any>>?>()
    val deleteResult: LiveData<Resource<GenericResponse<Any>>?> = _deleteResult

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

    fun updateBookingStatus(bookingId: Int, status: String) {
        viewModelScope.launch {
            _updateResult.postValue(Resource.Loading())
            try {
                val response = repository.updateBookingStatus(bookingId, status)
                if (response.isSuccessful && response.body() != null) {
                    _updateResult.postValue(Resource.Success(response.body()!!))
                } else {
                    _updateResult.postValue(Resource.Error("Gagal mengubah status booking."))
                }
            } catch (e: Exception) {
                _updateResult.postValue(Resource.Error("Terjadi kesalahan jaringan: ${e.message}"))
            }
        }
    }

    fun deleteBooking(bookingId: Int) {
        viewModelScope.launch {
            _deleteResult.postValue(Resource.Loading())
            try {
                val response = repository.deleteBooking(bookingId)
                if (response.isSuccessful && response.body() != null) {
                    _deleteResult.postValue(Resource.Success(response.body()!!))
                } else {
                    _deleteResult.postValue(Resource.Error("Gagal menghapus booking."))
                }
            } catch (e: Exception) {
                _deleteResult.postValue(Resource.Error("Terjadi kesalahan jaringan: ${e.message}"))
            }
        }
    }

    /**
     * Mereset LiveData updateResult kembali ke null agar event tidak terpicu berulang kali.
     */
    fun resetUpdateResult() {
        _updateResult.value = null
    }

    /**
     * Mereset LiveData deleteResult kembali ke null agar event tidak terpicu berulang kali.
     */
    fun resetDeleteResult() {
        _deleteResult.value = null
    }
}
