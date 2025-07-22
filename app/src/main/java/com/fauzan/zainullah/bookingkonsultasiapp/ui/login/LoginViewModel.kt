// file: ui/login/LoginViewModel.kt
package com.fauzan.zainullah.bookingkonsultasiapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.GenericResponse
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.LoginRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.LoginResponse
import com.fauzan.zainullah.bookingkonsultasiapp.data.repository.AuthRepository
import com.fauzan.zainullah.bookingkonsultasiapp.utils.Resource
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository = AuthRepository()

    // PERUBAHAN: LiveData sekarang berisi LoginResponse
    private val _loginResult = MutableLiveData<Resource<GenericResponse<LoginResponse>>>()
    val loginResult: LiveData<Resource<GenericResponse<LoginResponse>>> = _loginResult

    fun doLogin(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            _loginResult.postValue(Resource.Error("Username dan Password tidak boleh kosong."))
            return
        }

        viewModelScope.launch {
            _loginResult.postValue(Resource.Loading())
            try {
                val response = repository.login(LoginRequest(username, password))
                if (response.isSuccessful && response.body() != null) {
                    _loginResult.postValue(Resource.Success(response.body()!!))
                } else {
                    _loginResult.postValue(Resource.Error("Login Gagal: Username atau Password salah."))
                }
            } catch (e: Exception) {
                _loginResult.postValue(Resource.Error("Terjadi kesalahan jaringan: ${e.message}"))
            }
        }
    }
}
