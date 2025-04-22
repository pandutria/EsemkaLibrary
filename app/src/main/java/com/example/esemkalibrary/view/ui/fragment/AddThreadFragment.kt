package com.example.esemkalibrary.view.ui.fragment

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.esemkalibrary.R
import com.example.esemkalibrary.databinding.FragmentAddThreadBinding
import com.example.esemkalibrary.network.HttpHandler
import com.example.esemkalibrary.util.helper
import com.example.esemkalibrary.util.mySharedPrefrence
import com.example.esemkalibrary.view.ui.activity.MainActivity
import org.json.JSONObject

class AddThreadFragment : Fragment() {
    lateinit var binding: FragmentAddThreadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddThreadBinding.inflate(layoutInflater, container, false)

        binding.btn.setOnClickListener {
            if (binding.etBody.text == null || binding.etSubject.text == null) {
                helper.toast(requireContext(), "All field must be filled")
                return@setOnClickListener
            }

            sendData(this).execute()
        }

        return binding.root
    }

    class sendData (private var fragment: AddThreadFragment): AsyncTask< String, Void, String>() {
        override fun doInBackground(vararg p0: String?): String? {

            var json = JSONObject()
            json.put("subject", fragment.binding.etSubject.text)
            json.put("body", fragment.binding.etBody.text)

            return HttpHandler().request(
                "Forum",
                "POST",
                mySharedPrefrence.getToken(fragment.requireContext()),
                json.toString()
            )
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var code = JSONObject(result).getInt("code")
            helper.log(code.toString())
            if (code in 200 until 300) {
                var context = fragment.context
                if (context is MainActivity) {
                    context.showFragment(ForumFragment())
                }

//                if (context is )
            }
        }
    }

}