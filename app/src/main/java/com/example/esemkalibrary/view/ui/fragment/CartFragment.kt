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
import com.example.esemkalibrary.databinding.FragmentCartBinding
import com.example.esemkalibrary.network.HttpHandler
import com.example.esemkalibrary.util.cartManager
import com.example.esemkalibrary.util.helper
import com.example.esemkalibrary.util.mySharedPrefrence
import com.example.esemkalibrary.view.adapter.CartAdapter
import com.example.esemkalibrary.view.ui.activity.MainActivity
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CartFragment : Fragment() {
    lateinit var binding: FragmentCartBinding
    var start = ""
    var end = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(layoutInflater, container, false)

        binding.apply {
            rv.adapter = CartAdapter(cartManager.cartList)
            rv.layoutManager = LinearLayoutManager(context)

            val date = Date()
            val formatter = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
            etDateFrom.setText(formatter.format(date))

            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DATE, 3)
            etDateTo.setText(formatter.format(calendar.time))

            btn.setOnClickListener {
                if (cartManager.cartList.count() == 0) {
                    helper.toast(requireContext(), "Cart must be filled")
                    return@setOnClickListener
                }

                sendData(this@CartFragment).execute()
            }

        }

        return binding.root
    }

    class sendData(private var fragment: CartFragment):AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg p0: String?): String {

            var json = JSONObject()
            json.put("start", "2025-04-20T17:32:58.548Z")
            json.put("end", "2025-04-20T17:32:58.548Z")

            var ja = JSONArray()
            for (i in cartManager.cartList) {
                ja.put(i.book.id)
            }
            json.put("booksIds", ja)

            return HttpHandler().request(
                "Borrowing",
                "POST",
                mySharedPrefrence.getToken(fragment.requireContext()),
                json.toString()
            )
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var code = JSONObject(result).getInt("code")
            var body = JSONObject(result).getString("body")

            if (code in 200 until 300) {
                var context = fragment.context
                if (context is MainActivity) {
                    context.binding.btnHome.performClick()
                }
                cartManager.cartList.clear()
            }
        }
    }
}