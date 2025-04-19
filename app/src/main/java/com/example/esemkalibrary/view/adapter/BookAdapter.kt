package com.example.esemkalibrary.view.adapter

import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.esemkalibrary.R
import com.example.esemkalibrary.databinding.ItemBookBinding
import com.example.esemkalibrary.model.Book
import com.example.esemkalibrary.network.HttpHandler
import com.example.esemkalibrary.util.helper
import com.example.esemkalibrary.util.mySharedPrefrence
import com.example.esemkalibrary.view.ui.activity.MainActivity
import com.example.esemkalibrary.view.ui.fragment.DetailBookFragment
import com.example.esemkalibrary.view.ui.fragment.HomeFragment

class BookAdapter(private var fragment: HomeFragment,  private var bookList: List<Book>): RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemBookBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var book = bookList[position]
        holder.binding.apply {
            tvName.text = book.name
            tvAuthors.text = book.authors
            showImage(book.id!!, imgImage, fragment).execute()
        }

        holder.itemView.setOnClickListener {
            var context = holder.itemView.context
            if (context is MainActivity) {
                var fragment = DetailBookFragment()
                var bundle = Bundle()
                bundle.putString("book_id", book.id)
                fragment.arguments = bundle
                context.showFragment(fragment)
            }
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    class showImage(private var id: String, private var imageView: ImageView, private var fragment: HomeFragment): AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg p0: String?): Bitmap? {
            return try {
                HttpHandler().requestImage(
                    "Book/$id/Photo",
                    token = mySharedPrefrence.getToken(fragment.requireContext())
                )
            } catch (e: Exception) {
                helper.log(e.message!!)
                null
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            if (result != null) {
                imageView.setImageBitmap(result)
            } else {
                imageView.setImageResource(R.drawable.db2)
            }
        }
    }
}