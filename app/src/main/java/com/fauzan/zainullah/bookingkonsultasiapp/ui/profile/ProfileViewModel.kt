package com.fauzan.zainullah.bookingkonsultasiapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.UpdateProfileRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.User
import com.fauzan.zainullah.bookingkonsultasiapp.data.repository.ProfileRepository
import com.fauzan.zainullah.bookingkonsultasiapp.utils.Resource
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val repository = ProfileRepository()

    // LiveData untuk mengambil data profil
    private val _getProfileResult = MutableLiveData<Resource<User>>()
    val getProfileResult: LiveData<Resource<User>> = _getProfileResult

    // LiveData untuk hasil update profil
    private val _updateProfileResult = MutableLiveData<Resource<String>>()
    val updateProfileResult: LiveData<Resource<String>> = _updateProfileResult

    // LiveData untuk hasil hapus akun
    private val _deleteProfileResult = MutableLiveData<Resource<String>>()
    val deleteProfileResult: LiveData<Resource<String>> = _deleteProfileResult

    fun getProfile() {
        viewModelScope.launch {
            _getProfileResult.postValue(Resource.Loading())
            try {
                val response = repository.getProfile()
                if (response.isSuccessful && response.body()?.data != null) {
                    _getProfileResult.postValue(Resource.Success(response.body()!!.data!!))
                } else {
                    _getProfileResult.postValue(Resource.Error("Gagal memuat profil."))
                }
            } catch (e: Exception) {
                _getProfileResult.postValue(Resource.Error("Error jaringan: ${e.message}"))
            }
        }
    }

    fun updateProfile(request: UpdateProfileRequest) {
        viewModelScope.launch {
            _updateProfileResult.postValue(Resource.Loading())
            try {
                val response = repository.updateProfile(request)
                if (response.isSuccessful) {
                    _updateProfileResult.postValue(Resource.Success("Profil berhasil diperbarui."))
                } else {
                    _updateProfileResult.postValue(Resource.Error("Gagal memperbarui profil."))
                }
            } catch (e: Exception) {
                _updateProfileResult.postValue(Resource.Error("Error jaringan: ${e.message}"))
            }
        }
    }

    fun deleteProfile() {
        viewModelScope.launch {
            _deleteProfileResult.postValue(Resource.Loading())
            try {
                val response = repository.deleteProfile()
                if (response.isSuccessful) {
                    _deleteProfileResult.postValue(Resource.Success("Akun berhasil dihapus."))
                } else {
                    _deleteProfileResult.postValue(Resource.Error("Gagal menghapus akun."))
                }
            } catch (e: Exception) {
                _deleteProfileResult.postValue(Resource.Error("Error jaringan: ${e.message}"))
            }
        }
    }
}