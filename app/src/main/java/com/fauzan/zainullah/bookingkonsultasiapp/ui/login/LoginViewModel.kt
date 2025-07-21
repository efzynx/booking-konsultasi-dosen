package com.fauzan.zainullah.bookingkonsultasiapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.GenericResponse
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.LoginRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.User
import com.fauzan.zainullah.bookingkonsultasiapp.data.repository.AuthRepository
import com.fauzan.zainullah.bookingkonsultasiapp.utils.Resource
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _loginResult = MutableLiveData<Resource<GenericResponse<User>>>()
    val loginResult: LiveData<Resource<GenericResponse<User>>> = _loginResult

    fun doLogin(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            _loginResult.postValue(Resource.Error("Username atau password tidak boleh kosong"))
            return
        }


        viewModelScope.launch {
            _loginResult.postValue(Resource.Loading())
            try {
                val response = repository.login(LoginRequest(username, password))
                if (response.isSuccessful && response.body() != null) {
                    _loginResult.postValue(Resource.Success(response.body()!!))
                } else {
                    _loginResult.postValue(Resource.Error("Login gagal: Username atau password salah"))
                }
            } catch (e: Exception) {
                _loginResult.postValue(Resource.Error("Login gagal: ${e.message}"))
            }
        }


    }
}