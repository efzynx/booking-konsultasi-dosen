// file: ui/register/RegisterActivity.kt
package com.fauzan.zainullah.bookingkonsultasiapp.ui.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.RegisterRequest
import com.fauzan.zainullah.bookingkonsultasiapp.databinding.ActivityRegisterBinding
import com.fauzan.zainullah.bookingkonsultasiapp.utils.Resource

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val request = RegisterRequest(
                nama = binding.etNama.text.toString().trim(),
                nim = binding.etNimRegister.text.toString().trim(),
                username = binding.etUsernameRegister.text.toString().trim(),
                password = binding.etPasswordRegister.text.toString()
            )
            viewModel.doRegister(request)
        }
    }

    private fun setupObservers() {
        viewModel.registerResult.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBarRegister.visibility = View.VISIBLE
                    binding.btnRegister.isEnabled = false
                }
                is Resource.Success -> {
                    binding.progressBarRegister.visibility = View.GONE
                    Toast.makeText(this, "Registrasi Berhasil! Silakan login.", Toast.LENGTH_LONG).show()
                    finish() // Tutup halaman register dan kembali ke halaman login
                }
                is Resource.Error -> {
                    binding.progressBarRegister.visibility = View.GONE
                    binding.btnRegister.isEnabled = true
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}
