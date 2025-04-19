package com.example.esemkalibrary.view.ui.fragment

import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.esemkalibrary.R
import com.example.esemkalibrary.databinding.FragmentHomeBinding
import com.example.esemkalibrary.model.Book
import com.example.esemkalibrary.network.HttpHandler
import com.example.esemkalibrary.util.helper
import com.example.esemkalibrary.util.mySharedPrefrence
import com.example.esemkalibrary.view.adapter.BookAdapter
import org.json.JSONArray
import org.json.JSONObject

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        showData(this).execute()

        binding.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                showData(this@HomeFragment).execute()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        return binding.root
    }

    class showData(private var fragment: HomeFragment) : AsyncTask<Void, Void, Void>() {
        var bookList: MutableList<Book> = arrayListOf()
        override fun doInBackground(vararg p0: Void?): Void? {
            try {
                var jsonUrl = HttpHandler().request(
                    "Book",
                    token = mySharedPrefrence.getToken(fragment.requireContext())
                )

                if (fragment.binding.etSearch.text != null) {
                    bookList.clear()
                    jsonUrl = HttpHandler().request(
                        "Book?searchText=${fragment.binding.etSearch.text}",
                        token = mySharedPrefrence.getToken(fragment.requireContext())
                    )
                }

                var code = JSONObject(jsonUrl).getInt("code")
                var body = JSONObject(jsonUrl).getString("body")

                if (code in 200 until 300) {
                    var jsonArray = JSONArray(body)

                    for (i in 0 until jsonArray.length()) {
                        var book = jsonArray.getJSONObject(i)
                        bookList.add(
                            Book(
                                book.getString("id"),
                                book.getString("name"),
                                book.getString("authors")
                            )
                        )
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
                rv.adapter = BookAdapter(fragment, bookList)
                rv.layoutManager = GridLayoutManager(fragment.context, 2)
            }
        }
    }

}