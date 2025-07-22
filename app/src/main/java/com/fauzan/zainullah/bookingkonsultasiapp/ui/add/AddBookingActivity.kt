package com.fauzan.zainullah.bookingkonsultasiapp.ui.add

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fauzan.zainullah.bookingkonsultasiapp.R
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.AddBookingRequest
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.Booking
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.Dosen
import com.fauzan.zainullah.bookingkonsultasiapp.databinding.ActivityAddBookingBinding
import com.fauzan.zainullah.bookingkonsultasiapp.utils.Resource
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddBookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBookingBinding
    private val viewModel: AddBookingViewModel by viewModels()

    private var bookingToEdit: Booking? = null
    private var isEditMode = false

    private var dosenList: List<Dosen> = emptyList()
    private var selectedDosenId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("EXTRA_BOOKING")) {
            isEditMode = true
            bookingToEdit = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("EXTRA_BOOKING", Booking::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra("EXTRA_BOOKING")
            }
        }

        setupUI()
        setupObservers()
        setupListeners()
    }

    private fun setupUI() {
        if (isEditMode) {
            // PERBAIKAN: Menggunakan ID dari layout Anda (tv_add_title)
            binding.tvAddTitle.text = getString(R.string.title_edit_jadwal)
            binding.btnSave.text = getString(R.string.action_save_changes)

            // Sembunyikan field nama dan nim yang menggunakan TextInputLayout
            binding.tilNamaMahasiswa.visibility = View.GONE
            binding.tilNim.visibility = View.GONE

            bookingToEdit?.let {
                binding.etTopik.setText(it.topikKonsultasi)
                binding.tvSelectDate.text = it.tanggal
                binding.tvSelectTime.text = it.jam
            }
        } else {
            binding.tvAddTitle.text = getString(R.string.title_create_jadwal)
            binding.btnSave.text = getString(R.string.action_create_jadwal)
        }
    }

    private fun setupListeners() {
        binding.tvSelectDate.setOnClickListener { showDatePicker() }
        binding.tvSelectTime.setOnClickListener { showTimePicker() }
        binding.btnSave.setOnClickListener { saveBooking() }
    }

    private fun setupObservers() {
        viewModel.dosenList.observe(this) { resource ->
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
        }

        viewModel.createBookingResult.observe(this) { resource ->
            handleSaveResult(resource, "Jadwal berhasil disimpan!")
        }

        viewModel.updateBookingResult.observe(this) { resource ->
            handleSaveResult(resource, "Jadwal berhasil diperbarui!")
        }
    }

    private fun <T> handleSaveResult(resource: Resource<T>, successMessage: String) {
        when (resource) {
            is Resource.Loading -> {
                binding.progressBarAdd.visibility = View.VISIBLE
                binding.btnSave.isEnabled = false
            }
            is Resource.Success -> {
                binding.progressBarAdd.visibility = View.GONE
                binding.btnSave.isEnabled = true
                Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()
                finish()
            }
            is Resource.Error -> {
                binding.progressBarAdd.visibility = View.GONE
                binding.btnSave.isEnabled = true
                Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupDosenSpinner(dosen: List<Dosen>) {
        val dosenNames = dosen.map { it.namaDosen }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dosenNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDosen.adapter = adapter

        if (isEditMode) {
            val dosenIndex = dosenList.indexOfFirst { it.id == bookingToEdit?.dosenId }
            if (dosenIndex != -1) {
                binding.spinnerDosen.setSelection(dosenIndex)
            }
        }

        binding.spinnerDosen.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (dosenList.isNotEmpty()) {
                    selectedDosenId = dosenList[position].id
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.tvSelectDate.text = dateFormat.format(selectedCalendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
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
                binding.tvSelectTime.text = timeFormat.format(selectedCalendar.time)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun saveBooking() {
        val topik = binding.etTopik.text.toString().trim()
        val tanggal = binding.tvSelectDate.text.toString()
        val jam = binding.tvSelectTime.text.toString()

        if (topik.isEmpty() || tanggal.equals("Pilih Tanggal") || jam.equals("Pilih Jam") || selectedDosenId == -1) {
            Toast.makeText(this, "Topik, tanggal, jam, dan dosen harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        if (isEditMode) {
            viewModel.updateBooking(bookingToEdit!!.id, selectedDosenId, tanggal, jam, topik)
        } else {
            val namaMahasiswa = binding.etNamaMahasiswa.text.toString().trim()
            val nim = binding.etNim.text.toString().trim()

            if (namaMahasiswa.isEmpty() || nim.isEmpty()) {
                Toast.makeText(this, "Nama dan NIM harus diisi!", Toast.LENGTH_SHORT).show()
                return
            }

            val request = AddBookingRequest(
                namaMahasiswa = namaMahasiswa,
                nim = nim,
                dosenId = selectedDosenId,
                tanggal = tanggal,
                jam = jam,
                topikKonsultasi = topik
            )
            viewModel.createBooking(request)
        }
    }
}
