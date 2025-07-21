package com.fauzan.zainullah.bookingkonsultasiapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fauzan.zainullah.bookingkonsultasiapp.data.model.Booking
import com.fauzan.zainullah.bookingkonsultasiapp.databinding.ItemBookingBinding

class BookingAdapter(private val bookings: MutableList<Booking>) :
    RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = ItemBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(bookings[position])
    }

    override fun getItemCount(): Int = bookings.size

    fun updateData(newBookings: List<Booking>) {
        bookings.clear()
        bookings.addAll(newBookings)
        notifyDataSetChanged()
    }

    inner class BookingViewHolder(private val binding: ItemBookingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(booking: Booking) {
            binding.tvDosenName.text = booking.dosenInfo.namaDosen
            binding.tvTopic.text = booking.topikKonsultasi
            binding.tvDate.text = "${booking.tanggal} - ${booking.jam}"
            binding.tvStatus.text = booking.status
            // TODO: Tambahkan logika untuk mengubah warna status
        }
    }
}