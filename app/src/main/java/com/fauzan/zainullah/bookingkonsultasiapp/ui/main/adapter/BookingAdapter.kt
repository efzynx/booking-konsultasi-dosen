package com.fauzan.zainullah.bookingkonsultasiapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fauzan.zainullah.bookingkonsultasiapp.R
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.Booking
import com.fauzan.zainullah.bookingkonsultasiapp.databinding.ItemBookingBinding

class BookingAdapter(
    private val bookings: MutableList<Booking>,
    private val onItemClick: (Booking) -> Unit,
    private val onItemLongClick: (Booking) -> Unit
) :
    RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = ItemBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(bookings[position])
    }

    override fun getItemCount(): Int = bookings.size

    /**
     * Fungsi untuk memperbarui data di dalam adapter dan me-refresh RecyclerView.
     */
    fun updateData(newBookings: List<Booking>) {
        bookings.clear()
        bookings.addAll(newBookings)
        notifyDataSetChanged()
    }

    inner class BookingViewHolder(private val binding: ItemBookingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                // Memastikan posisi adapter valid sebelum memanggil listener
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick(bookings[adapterPosition])
                }
            }
            binding.root.setOnLongClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemLongClick(bookings[adapterPosition])
                }
                true // Mengembalikan true menandakan event sudah ditangani
            }
        }

        fun bind(booking: Booking) {
            binding.tvDosenName.text = booking.dosenInfo.namaDosen
            binding.tvTopic.text = booking.topikKonsultasi
            binding.tvDate.text = "${booking.tanggal} - ${booking.jam}"
            binding.tvStatus.text = booking.status.replaceFirstChar { it.uppercase() }

            val statusBackground = when (booking.status.lowercase()) {
                "approved" -> R.drawable.bg_status_approved
                "rejected" -> R.drawable.bg_status_rejected
                else -> R.drawable.bg_status_pending
            }
            binding.tvStatus.setBackgroundResource(statusBackground)
        }
    }
}
