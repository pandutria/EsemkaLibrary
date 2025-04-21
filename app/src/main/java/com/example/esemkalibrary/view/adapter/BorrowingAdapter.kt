package com.example.esemkalibrary.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.esemkalibrary.databinding.ItemBorrowingBinding
import com.example.esemkalibrary.model.Borrowing
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BorrowingAdapter(private var borrowingList: List<Borrowing>): RecyclerView.Adapter<BorrowingAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemBorrowingBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = ItemBorrowingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var borrowing = borrowingList[position]
        holder.binding.apply {
            tvStatus.text = borrowing.status
            tvBook.text = "${borrowing.bookCount} Books"

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

            val startDate = inputFormat.parse(borrowing.start)
            val endDate = inputFormat.parse(borrowing.end)

            tvDate.text = "${outputFormat.format(startDate)} - ${outputFormat.format(endDate)}"

        }
    }

    override fun getItemCount(): Int {
        return borrowingList.size
    }
}