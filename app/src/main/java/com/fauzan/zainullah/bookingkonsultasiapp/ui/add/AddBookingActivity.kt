// file: ui/add/AddBookingActivity.kt
package com.fauzan.zainullah.bookingkonsultasiapp.ui.add

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.AddBookingRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.Dosen
import com.fauzan.zainullah.bookingkonsultasiapp.databinding.ActivityAddBookingBinding
import com.fauzan.zainullah.bookingkonsultasiapp.utils.Resource
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddBookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBookingBinding
    private val viewModel: AddBookingViewModel by viewModels()

    private var selectedDate: String = ""
    private var selectedTime: String = ""
    private var selectedDosenId: Int = -1
    private var dosenList: List<Dosen> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.tvSelectDate.setOnClickListener { showDatePicker() }
        binding.tvSelectTime.setOnClickListener { showTimePicker() }
        binding.btnSave.setOnClickListener { saveBooking() }
    }

    private fun setupObservers() {
        viewModel.dosenList.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Loading -> binding.progressBarAdd.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBarAdd.visibility = View.GONE
                    dosenList = resource.data ?: emptyList()
                    setupDosenSpinner(dosenList)
                }
                is Resource.Error -> {
                    binding.progressBarAdd.visibility = View.GONE
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        viewModel.createBookingResult.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBarAdd.visibility = View.VISIBLE
                    binding.btnSave.isEnabled = false
                }
                is Resource.Success -> {
                    binding.progressBarAdd.visibility = View.GONE
                    Toast.makeText(this, "Jadwal berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    finish() // Kembali ke halaman utama
                }
                is Resource.Error -> {
                    binding.progressBarAdd.visibility = View.GONE
                    binding.btnSave.isEnabled = true
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setupDosenSpinner(dosen: List<Dosen>) {
        val dosenNames = dosen.map { it.namaDosen }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dosenNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDosen.adapter = adapter
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                selectedDate = dateFormat.format(selectedCalendar.time)
                binding.tvSelectDate.text = selectedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedCalendar.set(Calendar.MINUTE, minute)
                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                selectedTime = timeFormat.format(selectedCalendar.time)
                binding.tvSelectTime.text = selectedTime
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // 24 hour format
        )
        timePickerDialog.show()
    }

    private fun saveBooking() {
        val namaMahasiswa = binding.etNamaMahasiswa.text.toString().trim()
        val nim = binding.etNim.text.toString().trim()
        val topik = binding.etTopik.text.toString().trim()
        val selectedDosenPosition = binding.spinnerDosen.selectedItemPosition

        if (dosenList.isNotEmpty()) {
            selectedDosenId = dosenList[selectedDosenPosition].id
        }

        // Validasi input
        if (namaMahasiswa.isEmpty() || nim.isEmpty() || topik.isEmpty() || selectedDate.isEmpty() || selectedTime.isEmpty() || selectedDosenId == -1) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        val request = AddBookingRequest(
            namaMahasiswa = namaMahasiswa,
            nim = nim,
            dosenId = selectedDosenId,
            tanggal = selectedDate,
            jam = selectedTime,
            topikKonsultasi = topik
        )
        viewModel.createBooking(request)
    }
}
