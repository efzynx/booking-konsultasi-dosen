// file: ui/login/LoginActivity.kt
package com.fauzan.zainullah.bookingkonsultasiapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.fauzan.zainullah.bookingkonsultasiapp.databinding.ActivityLoginBinding
import com.fauzan.zainullah.bookingkonsultasiapp.ui.main.MainActivity
import com.fauzan.zainullah.bookingkonsultasiapp.ui.register.RegisterActivity
import com.fauzan.zainullah.bookingkonsultasiapp.utils.Resource
import com.fauzan.zainullah.bookingkonsultasiapp.utils.SessionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi SessionManager
        SessionManager.init(this)

        // PENAMBAHAN: Cek sesi sebelum menampilkan layout
        checkSession()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupObservers()
    }

    private fun checkSession() {
        if (SessionManager.isLoggedIn()) {
            navigateToMain()
        }
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.doLogin(username, password)
        }

        binding.tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupObservers() {
        viewModel.loginResult.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnLogin.isEnabled = false
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnLogin.isEnabled = true

                    Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show()

                    // PENAMBAHAN: Simpan sesi pengguna
                    val user = resource.data?.data
                    SessionManager.saveSession(true, user)

                    navigateToMain()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Tutup LoginActivity agar tidak bisa kembali
    }
}
