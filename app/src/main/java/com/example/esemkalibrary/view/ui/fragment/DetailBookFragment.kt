package com.example.esemkalibrary.view.ui.fragment

import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.esemkalibrary.R
import com.example.esemkalibrary.databinding.FragmentDetailBookBinding
import com.example.esemkalibrary.model.Book
import com.example.esemkalibrary.model.Cart
import com.example.esemkalibrary.network.HttpHandler
import com.example.esemkalibrary.util.cartManager
import com.example.esemkalibrary.util.helper
import com.example.esemkalibrary.util.mySharedPrefrence
import com.example.esemkalibrary.view.ui.activity.MainActivity
import org.json.JSONObject

class DetailBookFragment : Fragment() {
    lateinit var binding: FragmentDetailBookBinding
    var book_id = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBookBinding.inflate(layoutInflater, container, false)

        book_id = arguments?.getString("book_id").toString()
        showData(this).execute()

        binding.apply {
            btnAdd.setOnClickListener {
                cartManager.cartList.add(Cart(
                    tvIsbn.text.toString(),
                    tvPublish.text.toString(),
                    tvAvailable.text.toString(),
                    tvDesc.text.toString(),
                    Book(
                        book_id,
                        tvName.text.toString(),
                        tvAuthors.text.toString()
                    )
                ))

                var context = context
                if (context is MainActivity) {
                    context.binding.btnMyCart.performClick()
                }
            }
        }

        return binding.root
    }
    class showData(private var fragment: DetailBookFragment): AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg p0: String?): String {
            return HttpHandler().request(
                "Book/${fragment.book_id}",
                token = mySharedPrefrence.getToken(fragment.requireContext())
            )
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var code = JSONObject(result).getInt("code")
            var body = JSONObject(result).getString("body")

            if (code in 200 until 300) {
                var book = JSONObject(body)
                fragment.binding.apply {
                    tvName.text = book.getString("name")
                    tvAuthors.text = book.getString("authors")
                    tvIsbn.text = book.getString("isbn")
                    tvPublish.text = book.getString("publisher")
                    tvAvailable.text = book.getInt("available").toString()
                    tvDesc.text = book.getString("description")
                    showImage(book.getString("id"), imgImage, fragment).execute()
                }
            }
        }
    }

    class showImage(private var id: String, private var imageView: ImageView, private var fragment: DetailBookFragment): AsyncTask<String, Void, Bitmap>() {
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