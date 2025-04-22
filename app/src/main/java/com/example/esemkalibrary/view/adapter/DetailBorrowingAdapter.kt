package com.example.esemkalibrary.view.adapter

import android.graphics.Bitmap
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.esemkalibrary.R
import com.example.esemkalibrary.databinding.ItemDetailBorrowingBinding
import com.example.esemkalibrary.model.Borrowing
import com.example.esemkalibrary.model.Cart
import com.example.esemkalibrary.network.HttpHandler
import com.example.esemkalibrary.util.helper
import com.example.esemkalibrary.util.mySharedPrefrence
import com.example.esemkalibrary.view.ui.fragment.HomeFragment

class DetailBorrowingAdapter(private var cartList: List<Cart>): RecyclerView.Adapter<DetailBorrowingAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemDetailBorrowingBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = ItemDetailBorrowingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var cart = cartList[position]
        holder.binding.apply {
            tvName.text = cart.book.name
            tvAuthors.text = cart.book.authors
            tvIsbn.text = cart.isbn
            showImage(cart.book.id!!, imgImage)
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    class showImage(private var id: String, private var imageView: ImageView): AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg p0: String?): Bitmap? {
            return try {
                HttpHandler().requestImage(
                    "Book/$id/Photo",
                    token = mySharedPrefrence.getToken(imageView.context)
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