// file: ui/profile/ProfileActivity.kt
package com.fauzan.zainullah.bookingkonsultasiapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.fauzan.zainullah.bookingkonsultasiapp.R
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.UpdateProfileRequest
import com.fauzan.zainullah.bookingkonsultasiapp.databinding.ActivityProfileBinding
import com.fauzan.zainullah.bookingkonsultasiapp.ui.login.LoginActivity
import com.fauzan.zainullah.bookingkonsultasiapp.utils.Resource
import com.fauzan.zainullah.bookingkonsultasiapp.utils.SessionManager

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupObservers()

        viewModel.getProfile() // Ambil data profil saat activity dibuat
    }

    private fun setupListeners() {
        binding.btnSaveProfile.setOnClickListener {
            val nama = binding.etNamaProfile.text.toString().trim()
            val username = binding.etUsernameProfile.text.toString().trim()
            val password = binding.etPasswordProfile.text.toString()

            // PERBAIKAN: Request ini sekarang tidak lagi membutuhkan 'nim'
            val request = UpdateProfileRequest(
                nama = nama.ifEmpty { null },
                username = username.ifEmpty { null },
                password = password.ifEmpty { null }
            )
            viewModel.updateProfile(request)
        }

        binding.btnDeleteAccount.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun setupObservers() {
        // Observer untuk mengambil data profil
        viewModel.getProfileResult.observe(this, Observer { resource ->
            when(resource) {
                is Resource.Loading -> { /* Tampilkan loading */ }
                is Resource.Success -> {
                    resource.data?.let { user ->
                        // PERBAIKAN: 'user.nim' sekarang akan dikenali
                        binding.tvNimValue.text = user.nim ?: "N/A"
                        binding.etNamaProfile.setText(user.nama)
                        binding.etUsernameProfile.setText(user.username)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        // Observer untuk hasil update profil
        viewModel.updateProfileResult.observe(this, Observer { resource ->
            when(resource) {
                is Resource.Loading -> { /* Tampilkan loading */ }
                is Resource.Success -> {
                    Toast.makeText(this, resource.data, Toast.LENGTH_SHORT).show()
                    finish() // Kembali ke halaman sebelumnya
                }
                is Resource.Error -> {
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        // Observer untuk hasil hapus akun
        viewModel.deleteProfileResult.observe(this, Observer { resource ->
            when(resource) {
                is Resource.Loading -> { /* Tampilkan loading */ }
                is Resource.Success -> {
                    Toast.makeText(this, resource.data, Toast.LENGTH_LONG).show()
                    // Hapus sesi dan kembali ke halaman login
                    SessionManager.clearSession()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                is Resource.Error -> {
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun showDeleteConfirmationDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_delete_confirmation, null)
        val confirmationEditText = dialogView.findViewById<EditText>(R.id.et_delete_confirmation)
        val confirmationText = "ya, hapus akun saya!"

        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Konfirmasi Hapus Akun")
            .setPositiveButton("Hapus", null) // Set null dulu agar dialog tidak langsung tertutup
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()

        // Override listener tombol positif
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val inputText = confirmationEditText.text.toString()
            if (inputText == confirmationText) {
                viewModel.deleteProfile()
                dialog.dismiss()
            } else {
                confirmationEditText.error = "Teks konfirmasi tidak sesuai!"
            }
        }
    }
}
