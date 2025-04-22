package com.example.esemkalibrary.view.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.esemkalibrary.databinding.FragmentDetailBorrowingBinding
import com.example.esemkalibrary.databinding.ItemBorrowingBinding
import com.example.esemkalibrary.model.Borrowing
import com.example.esemkalibrary.view.ui.activity.MainActivity
import com.example.esemkalibrary.view.ui.fragment.DetailBorrowingFragment
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

            holder.itemView.setOnClickListener {
                var context = holder.itemView.context
                if (context is MainActivity) {
                    var fragment = DetailBorrowingFragment()
                    var bundle = Bundle()
                    bundle.putString("id", borrowing.id)
                    bundle.putString("date", tvDate.text.toString())
                    bundle.putString("status", tvStatus.text.toString())
                    fragment.arguments = bundle

                    context.showFragment(fragment)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return borrowingList.size
    }
}