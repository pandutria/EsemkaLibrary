package com.example.esemkalibrary.view.ui.fragment

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.esemkalibrary.R
import com.example.esemkalibrary.databinding.FragmentDetailBookBinding
import com.example.esemkalibrary.databinding.FragmentDetailBorrowingBinding
import com.example.esemkalibrary.databinding.ItemDetailBorrowingBinding
import com.example.esemkalibrary.model.Book
import com.example.esemkalibrary.model.Borrowing
import com.example.esemkalibrary.model.Cart
import com.example.esemkalibrary.network.HttpHandler
import com.example.esemkalibrary.util.helper
import com.example.esemkalibrary.util.mySharedPrefrence
import com.example.esemkalibrary.view.adapter.DetailBorrowingAdapter
import org.json.JSONObject

class DetailBorrowingFragment : Fragment() {
    lateinit var binding: FragmentDetailBorrowingBinding
    var borrowing_Id = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBorrowingBinding.inflate(layoutInflater, container, false)

        binding.apply {
            tvDate.text = arguments?.getString("date")
            tvStatus.text = arguments?.getString("status")
            borrowing_Id = arguments?.getString("id").toString()


        }

        showData(this).execute()

        return binding.root
    }

    class showData(private var fragment: DetailBorrowingFragment): AsyncTask<Void, Void, Void>() {
        var cartList: MutableList<Cart> = arrayListOf()
        override fun doInBackground(vararg p0: Void?): Void? {

            try {
                var jsonUrl = HttpHandler().request(
                    "Borrowing/${fragment.borrowing_Id}",
                    token = mySharedPrefrence.getToken(fragment.requireContext())
                )

                var code = JSONObject(jsonUrl).getInt("code")
                var body = JSONObject(jsonUrl).getString("body")

                if (code in 200 until 300) {
                    var res = JSONObject(body)
                    var jsonArray = res.getJSONArray("bookBorrowings")

                    for (i in 0 until jsonArray.length()) {
                        var borrowing = jsonArray.getJSONObject(i)
                        var book = borrowing.getJSONObject("book")

                        cartList.add(Cart(
                            book.getString("isbn"),
                            book.getString("publisher"),
                            book.getInt("available").toString(),

                            book =  Book(
                                id = book.getString("id"),
                                name = book.getString("name"),
                                authors = book.getString("authors")
                            )
                        ))
                    }
                }
            } catch (e: Exception) {
                helper.log(e.message!!)
            }

            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            fragment.binding.apply {
                rv.adapter = DetailBorrowingAdapter(cartList)
                rv.layoutManager = LinearLayoutManager(fragment.context)
            }
        }
    }

}