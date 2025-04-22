package com.example.esemkalibrary.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.esemkalibrary.databinding.ItemDetailThreadBinding
import com.example.esemkalibrary.model.Forum
import com.example.esemkalibrary.util.helper
import com.example.esemkalibrary.view.ui.fragment.DetailThreadFragment
import java.text.SimpleDateFormat
import java.util.Locale

class DetailThreadAdapter(private var fragment: DetailThreadFragment, private var threadList: List<Forum>): RecyclerView.Adapter<DetailThreadAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemDetailThreadBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = ItemDetailThreadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var thread = threadList[position]
        holder.binding.apply {
            var input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            var output = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            var parse = input.parse(thread.createdAt)

            tvName.text = "${thread.name} - ${output.format(parse)}"
            tvMessage.text = thread.message

            if (thread.email == helper.email) {
                btn.visibility = View.VISIBLE
            } else {
                btn.visibility = View.GONE
            }

            btn.setOnClickListener {
                var builder = AlertDialog.Builder(fragment.requireContext())
                builder.setTitle("Confirmation")
                builder.setMessage("Are you sure want to delete this data?")

                builder.setPositiveButton("Yes") {dialog, _ ->
                    fragment.deleteData(fragment, thread.id!!).execute()
                    dialog.dismiss()
                }

                builder.setNegativeButton("No") {dialog, _ ->
                    dialog.dismiss()
                }

                var dialog = builder.create()
                dialog.show()

            }
        }
    }

    override fun getItemCount(): Int {
        return threadList.size
    }

    fun showDialog(context: Context) {

    }
}