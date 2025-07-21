// file: ui/add/AddBookingViewModel.kt
package com.fauzan.zainullah.bookingkonsultasiapp.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.AddBookingRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.Dosen
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.GenericResponse
import com.fauzan.zainullah.bookingkonsultasiapp.data.repository.BookingRepository
import com.fauzan.zainullah.bookingkonsultasiapp.data.repository.DosenRepository
import com.fauzan.zainullah.bookingkonsultasiapp.utils.Resource
import kotlinx.coroutines.launch

class AddBookingViewModel : ViewModel() {

    private val dosenRepository = DosenRepository()
    private val bookingRepository = BookingRepository()

    // LiveData untuk daftar dosen
    private val _dosenList = MutableLiveData<Resource<List<Dosen>>>()
    val dosenList: LiveData<Resource<List<Dosen>>> = _dosenList

    // LiveData untuk hasil simpan booking
    private val _createBookingResult = MutableLiveData<Resource<GenericResponse<Any>>>()
    val createBookingResult: LiveData<Resource<GenericResponse<Any>>> = _createBookingResult

    init {
        fetchDosenList() // Langsung ambil daftar dosen saat ViewModel dibuat
    }

    private fun fetchDosenList() {
        viewModelScope.launch {
            _dosenList.postValue(Resource.Loading())
            try {
                val response = dosenRepository.getAllDosen()
                if (response.isSuccessful && response.body() != null) {
                    _dosenList.postValue(Resource.Success(response.body()!!.data!!))
                } else {
                    _dosenList.postValue(Resource.Error("Gagal mengambil daftar dosen."))
                }
            } catch (e: Exception) {
                _dosenList.postValue(Resource.Error("Error jaringan: ${e.message}"))
            }
        }
    }

    fun createBooking(request: AddBookingRequest) {
        viewModelScope.launch {
            _createBookingResult.postValue(Resource.Loading())
            try {
                val response = bookingRepository.createBooking(request)
                if (response.isSuccessful && response.body() != null) {
                    _createBookingResult.postValue(Resource.Success(response.body()!!))
                } else {
                    _createBookingResult.postValue(Resource.Error("Gagal menyimpan jadwal."))
                }
            } catch (e: Exception) {
                _createBookingResult.postValue(Resource.Error("Error jaringan: ${e.message}"))
            }
        }
    }
}
