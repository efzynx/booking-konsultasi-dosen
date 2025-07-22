package com.fauzan.zainullah.bookingkonsultasiapp.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fauzan.zainullah.bookingkonsultasiapp.R
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.Booking
import com.fauzan.zainullah.bookingkonsultasiapp.databinding.ActivityBookingDetailBinding

class BookingDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingDetailBinding
    private var booking: Booking? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup toolbar dengan tombol kembali
        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Ambil data booking dari intent
        booking = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_BOOKING", Booking::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_BOOKING")
        }

        // Jika data ada, tampilkan ke UI
        if (booking != null) {
            populateUI(booking!!)
            setupActionButtons(booking!!)
        } else {
            // Jika tidak ada data, tutup activity
            finish()
        }
    }

    private fun populateUI(booking: Booking) {
        binding.tvDetailNamaMahasiswa.text = booking.namaMahasiswa
        binding.tvDetailNim.text = booking.nim
        binding.tvDetailWaktu.text = "${booking.tanggal} - ${booking.jam}"
        binding.tvDetailTopik.text = booking.topikKonsultasi
        binding.tvDetailStatus.text = booking.status.replaceFirstChar { it.uppercase() }

        // Atur warna status
        val statusBackground = when (booking.status.lowercase()) {
            "approved" -> R.drawable.bg_status_approved
            "rejected", "declined" -> R.drawable.bg_status_rejected
            else -> R.drawable.bg_status_pending
        }
        binding.tvDetailStatus.setBackgroundResource(statusBackground)
    }

    private fun setupActionButtons(booking: Booking) {
        // Hanya tampilkan tombol jika status masih "pending"
        if (!booking.status.equals("pending", ignoreCase = true)) {
            binding.layoutActions.visibility = View.GONE
        }

        binding.btnReject.setOnClickListener {
            sendResult("rejected")
        }

        binding.btnApprove.setOnClickListener {
            sendResult("approved")
        }
    }

    private fun sendResult(newStatus: String) {
        val resultIntent = Intent()
        resultIntent.putExtra("BOOKING_ID", booking?.id)
        resultIntent.putExtra("NEW_STATUS", newStatus)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    // Fungsi untuk handle klik tombol kembali di toolbar
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
