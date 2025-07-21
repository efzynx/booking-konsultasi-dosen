// file: ui/register/RegisterViewModel.kt
package com.fauzan.zainullah.bookingkonsultasiapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.GenericResponse
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.RegisterRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.repository.AuthRepository
import com.fauzan.zainullah.bookingkonsultasiapp.utils.Resource
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _registerResult = MutableLiveData<Resource<GenericResponse<Any>>>()
    val registerResult: LiveData<Resource<GenericResponse<Any>>> = _registerResult

    fun doRegister(request: RegisterRequest) {
        // Validasi
        if (request.nama.isEmpty() || request.nim.isEmpty() || request.username.isEmpty() || request.password.isEmpty()) {
            _registerResult.postValue(Resource.Error("Semua field harus diisi."))
            return
        }

        viewModelScope.launch {
            _registerResult.postValue(Resource.Loading())
            try {
                val response = repository.register(request)
                if (response.isSuccessful && response.body() != null) {
                    _registerResult.postValue(Resource.Success(response.body()!!))
                } else {
                    _registerResult.postValue(Resource.Error("Registrasi gagal. Username atau NIM mungkin sudah terdaftar."))
                }
            } catch (e: Exception) {
                _registerResult.postValue(Resource.Error("Terjadi kesalahan jaringan: ${e.message}"))
            }
        }
    }
}
