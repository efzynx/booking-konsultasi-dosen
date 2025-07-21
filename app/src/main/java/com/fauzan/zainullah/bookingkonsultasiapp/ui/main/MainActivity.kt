package com.fauzan.zainullah.bookingkonsultasiapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fauzan.zainullah.bookingkonsultasiapp.databinding.ActivityMainBinding
import com.fauzan.zainullah.bookingkonsultasiapp.ui.add.AddBookingActivity
import com.fauzan.zainullah.bookingkonsultasiapp.ui.main.adapter.BookingAdapter
import com.fauzan.zainullah.bookingkonsultasiapp.utils.Resource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var bookingAdapter: BookingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        setupListeners()

        viewModel.fetchBookings() // Langsung panggil API saat activity dibuat
    }

    private fun setupRecyclerView() {
        bookingAdapter = BookingAdapter(mutableListOf())
        binding.rvBookings.apply {
            adapter = bookingAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setupListeners() {
        binding.fabAdd.setOnClickListener {
            // Pindah ke halaman Tambah Booking
            startActivity(Intent(this, AddBookingActivity::class.java))
        }
    }

    private fun setupObservers() {
        viewModel.bookingResult.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBarMain.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBarMain.visibility = View.GONE
                    resource.data?.data?.let { bookings ->
                        bookingAdapter.updateData(bookings)
                    }
                }
                is Resource.Error -> {
                    binding.progressBarMain.visibility = View.GONE
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    // Panggil fetchBookings() lagi saat kembali ke activity ini
    override fun onResume() {
        super.onResume()
        viewModel.fetchBookings()
    }
}