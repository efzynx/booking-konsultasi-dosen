package com.fauzan.zainullah.bookingkonsultasiapp.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fauzan.zainullah.bookingkonsultasiapp.R
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.Booking
import com.fauzan.zainullah.bookingkonsultasiapp.databinding.ActivityMainBinding
import com.fauzan.zainullah.bookingkonsultasiapp.ui.add.AddBookingActivity
import com.fauzan.zainullah.bookingkonsultasiapp.ui.detail.BookingDetailActivity
import com.fauzan.zainullah.bookingkonsultasiapp.ui.login.LoginActivity
import com.fauzan.zainullah.bookingkonsultasiapp.ui.main.adapter.BookingAdapter
import com.fauzan.zainullah.bookingkonsultasiapp.ui.profile.ProfileActivity
import com.fauzan.zainullah.bookingkonsultasiapp.utils.Resource
import com.fauzan.zainullah.bookingkonsultasiapp.utils.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var bookingAdapter: BookingAdapter
    // PENAMBAHAN: Variabel untuk menyimpan daftar asli
    private var masterBookingList: List<Booking> = listOf()

    // PENAMBAHAN: Launcher untuk menerima hasil dari BookingDetailActivity
    private val detailResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val bookingId = data?.getIntExtra("BOOKING_ID", -1)
            val newStatus = data?.getStringExtra("NEW_STATUS")

            if (bookingId != -1 && newStatus != null) {
                // Panggil fungsi update di ViewModel
                viewModel.updateBookingStatus(bookingId!!, newStatus)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Jadwal Konsultasi"

        SessionManager.init(this)

        setupRecyclerView()
        setupObservers()
        setupListeners()

//        checkUserRoleForFab()
        setupRoleSpecificUI()
        setupUserGreeting()
    }

    private fun setupListeners() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddBookingActivity::class.java))
        }

        // PENAMBAHAN: Listener untuk ChipGroup
        binding.chipGroupFilter.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) {
                binding.chipAll.isChecked = true
                return@setOnCheckedStateChangeListener
            }

            val selectedStatus = when (checkedIds.first()) {
                R.id.chip_pending -> "pending"
                R.id.chip_approved -> "approved"
                R.id.chip_rejected -> "rejected" // atau "declined"
                else -> "all"
            }
            filterBookingsByStatus(selectedStatus)
        }
    }

    private fun setupRecyclerView() {
        bookingAdapter = BookingAdapter(
            mutableListOf(),
            onItemClick = { booking ->
                handleBookingClick(booking)
            },
            onItemLongClick = { booking ->
                handleBookingLongClick(booking)
            }
        )
        binding.rvBookings.apply {
            adapter = bookingAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

//    private fun setupObservers() {
//        viewModel.bookingResult.observe(this, Observer { resource ->
//            when (resource) {
//                is Resource.Loading -> {
//                    binding.progressBarMain.visibility = View.VISIBLE
//                }
//                is Resource.Success -> {
//                    binding.progressBarMain.visibility = View.GONE
//                    resource.data?.data?.let { bookings ->
//                        bookingAdapter.updateData(bookings)
//                    }
//                }
//                is Resource.Error -> {
//                    binding.progressBarMain.visibility = View.GONE
//                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
//                }
//            }
//        })
    private fun setupObservers() {
        viewModel.bookingResult.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBarMain.visibility = View.VISIBLE
                    binding.rvBookings.visibility = View.GONE
                    binding.layoutEmptyState.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBarMain.visibility = View.GONE
                    // Simpan daftar asli ke master list
                    masterBookingList = resource.data?.data ?: emptyList()

                    // Terapkan filter yang sedang aktif
                    val checkedChipId = binding.chipGroupFilter.checkedChipId
                    val currentFilter = when (checkedChipId) {
                        R.id.chip_pending -> "pending"
                        R.id.chip_approved -> "approved"
                        R.id.chip_rejected -> "rejected"
                        else -> "all"
                    }
                    filterBookingsByStatus(currentFilter)
                }
                is Resource.Error -> {
                    binding.progressBarMain.visibility = View.GONE
                    binding.layoutEmptyState.visibility = View.VISIBLE
                    binding.rvBookings.visibility = View.GONE
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        viewModel.updateResult.observe(this, Observer { resource ->
            // Gunakan 'if' untuk memastikan observer hanya berjalan jika ada data baru
            if (resource != null) {
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBarMain.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBarMain.visibility = View.GONE
                        Toast.makeText(this, "Status berhasil diubah", Toast.LENGTH_SHORT).show()
                        viewModel.fetchBookings() // Refresh daftar
                    }
                    is Resource.Error -> {
                        binding.progressBarMain.visibility = View.GONE
                        Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                    }
                }
                // Reset LiveData setelah event ditangani untuk mencegah loop
                viewModel.resetUpdateResult()
            }
        })

        viewModel.deleteResult.observe(this, Observer { resource ->
            // Gunakan 'if' untuk memastikan observer hanya berjalan jika ada data baru
            if (resource != null) {
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBarMain.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBarMain.visibility = View.GONE
                        Toast.makeText(this, "Jadwal berhasil dihapus", Toast.LENGTH_SHORT).show()
                        viewModel.fetchBookings() // Refresh daftar
                    }
                    is Resource.Error -> {
                        binding.progressBarMain.visibility = View.GONE
                        Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                    }
                }
                // Reset LiveData setelah event ditangani untuk mencegah loop
                viewModel.resetDeleteResult()
            }
        })
    }


    // PENAMBAHAN: Fungsi baru untuk memfilter daftar
    private fun filterBookingsByStatus(status: String) {
        val filteredList = if (status == "all") {
            masterBookingList
        } else {
            masterBookingList.filter { it.status.equals(status, ignoreCase = true) }
        }

        bookingAdapter.updateData(filteredList)

        // Perbarui juga tampilan empty state berdasarkan hasil filter
        if (filteredList.isEmpty()) {
            binding.rvBookings.visibility = View.GONE
            binding.layoutEmptyState.visibility = View.VISIBLE
        } else {
            binding.rvBookings.visibility = View.VISIBLE
            binding.layoutEmptyState.visibility = View.GONE
        }
    }


    // PERUBAHAN: Mengganti nama fungsi dan menambahkan logika untuk filter
    private fun setupRoleSpecificUI() {
        val userRole = SessionManager.getUserData()?.role
        if (userRole == "dosen") {
            binding.fabAdd.visibility = View.GONE
            binding.chipGroupFilter.visibility = View.VISIBLE
        } else {
            binding.fabAdd.visibility = View.VISIBLE
            binding.chipGroupFilter.visibility = View.GONE
        }
    }


    private fun handleBookingLongClick(booking: Booking) {
        val userRole = SessionManager.getUserData()?.role
        if (userRole == "mahasiswa") {
            showDeleteConfirmationDialog(booking)
        }
    }

    private fun showDeleteConfirmationDialog(booking: Booking) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Jadwal")
            .setMessage("Apakah Anda yakin ingin menghapus jadwal konsultasi ini?")
            .setPositiveButton("Hapus") { dialog, _ ->
                viewModel.deleteBooking(booking.id)
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setupUserGreeting() {
        val user = SessionManager.getUserData()
        if (user != null) {
            val role = user.role.replaceFirstChar { it.uppercase() }
            binding.tvUserGreeting.text = getString(R.string.user_greeting, role, user.nama)
            binding.tvUserGreeting.visibility = View.VISIBLE
        } else {
            binding.tvUserGreeting.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            R.id.action_logout -> {
                performLogout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun performLogout() {
        SessionManager.clearSession()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

//    private fun setupListeners() {
//        binding.fabAdd.setOnClickListener {
//            startActivity(Intent(this, AddBookingActivity::class.java))
//        }
//    }

    private fun checkUserRoleForFab() {
        val userRole = SessionManager.getUserData()?.role
        if (userRole == "dosen") {
            binding.fabAdd.visibility = View.GONE
        } else {
            binding.fabAdd.visibility = View.VISIBLE
        }
    }

    private fun handleBookingClick(booking: Booking) {
        val userRole = SessionManager.getUserData()?.role

        if (userRole == "dosen") {
            val intent = Intent(this, BookingDetailActivity::class.java)
            intent.putExtra("EXTRA_BOOKING", booking)
            detailResultLauncher.launch(intent)
//            if (booking.status.equals("pending", ignoreCase = true)) {
//                showDosenActionDialog(booking)
//            } else {
//                Toast.makeText(this, "Tindakan tidak diizinkan untuk status ini", Toast.LENGTH_SHORT).show()
//            }
        } else if (userRole == "mahasiswa") {
            if (booking.status.equals("pending", ignoreCase = true)) {
                val intent = Intent(this, AddBookingActivity::class.java)
                intent.putExtra("EXTRA_BOOKING", booking)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Jadwal yang sudah diproses tidak bisa diubah", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDosenActionDialog(booking: Booking) {
        val options = arrayOf("Approve", "Decline")
        AlertDialog.Builder(this)
            .setTitle("Pilih Tindakan")
            .setItems(options) { dialog, which ->
                val newStatus = if (which == 0) "approved" else "rejected"
                viewModel.updateBookingStatus(booking.id, newStatus)
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchBookings()
    }
}
