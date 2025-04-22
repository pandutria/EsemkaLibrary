package com.example.esemkalibrary.view.ui.fragment

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ThemedSpinnerAdapter.Helper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.esemkalibrary.R
import com.example.esemkalibrary.databinding.FragmentProfileBinding
import com.example.esemkalibrary.model.Book
import com.example.esemkalibrary.model.Borrowing
import com.example.esemkalibrary.model.Cart
import com.example.esemkalibrary.network.HttpHandler
import com.example.esemkalibrary.util.helper
import com.example.esemkalibrary.util.mySharedPrefrence
import com.example.esemkalibrary.view.adapter.BorrowingAdapter
import com.example.esemkalibrary.view.ui.activity.LoginActivity
import com.example.esemkalibrary.view.ui.activity.MainActivity
import org.json.JSONArray
import org.json.JSONObject

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        meData(this).execute()
        showData(this).execute()

        binding.btn.setOnClickListener {
            mySharedPrefrence.deleteToken(requireContext())

            var context = context
            if (context is MainActivity) {
                context.startActivity(Intent(context, LoginActivity::class.java))
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        meData(this).execute()
        showData(this).execute()
    }

    class showData(private var fragment: ProfileFragment) : AsyncTask<Void, Void, Void>() {
        var borrowList: MutableList<Borrowing> = arrayListOf()
        override fun doInBackground(vararg p0: Void?): Void? {

            try {
                var jsonUrl = HttpHandler().request(
                    "Borrowing",
                    token = mySharedPrefrence.getToken(fragment.requireContext())
                )

                var code = JSONObject(jsonUrl).getInt("code")
                var body = JSONObject(jsonUrl).getString("body")

                if (code in 200 until 300) {
                    var jsonArray = JSONArray(body)

                    for (i in 0 until jsonArray.length()) {
                        var res = jsonArray.getJSONObject(i)

                        borrowList.add(Borrowing(
                            res.getString("id"),
                            res.getString("start"),
                            res.getString("end"),
                            res.getString("returnedAt"),
                            res.getInt("bookCount"),
                            res.getString("status"),
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
                rv.adapter = BorrowingAdapter(borrowList)
                rv.layoutManager = LinearLayoutManager(fragment.context)
            }
        }
    }

    class meData(private var fragment: ProfileFragment) : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg p0: String?): String {
            return HttpHandler().request(
                "User/Me",
                token = mySharedPrefrence.getToken(fragment.requireContext())
            )
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var code = JSONObject(result).getInt("code")
            var body = JSONObject(result).getString("body")

            if (code in 200 until 300) {
                fragment.binding.apply {
                    tvEmail.text = JSONObject(body).getString("email")
                    tvName.text = JSONObject(body).getString("name")
                }
            }
        }
    }
}