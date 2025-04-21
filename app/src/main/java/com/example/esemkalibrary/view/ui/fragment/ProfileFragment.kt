package com.example.esemkalibrary.view.ui.fragment

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ThemedSpinnerAdapter.Helper
import com.example.esemkalibrary.R
import com.example.esemkalibrary.databinding.FragmentProfileBinding
import com.example.esemkalibrary.network.HttpHandler
import com.example.esemkalibrary.util.helper
import com.example.esemkalibrary.util.mySharedPrefrence
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

        return binding.root

    }
    class meData(private var fragment: ProfileFragment): AsyncTask<String, Void, String>() {
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