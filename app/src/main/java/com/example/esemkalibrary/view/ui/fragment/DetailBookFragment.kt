package com.example.esemkalibrary.view.ui.fragment

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.esemkalibrary.R
import com.example.esemkalibrary.databinding.FragmentDetailBookBinding
import com.example.esemkalibrary.network.HttpHandler
import com.example.esemkalibrary.util.mySharedPrefrence
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

                }
            }
        }
    }


}