package com.example.esemkalibrary.view.ui.fragment

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.esemkalibrary.R
import com.example.esemkalibrary.databinding.FragmentDetailThreadBinding
import com.example.esemkalibrary.model.Forum
import com.example.esemkalibrary.network.HttpHandler
import com.example.esemkalibrary.util.helper
import com.example.esemkalibrary.util.mySharedPrefrence
import com.example.esemkalibrary.view.adapter.DetailThreadAdapter
import org.json.JSONObject

class DetailThreadFragment : Fragment() {
    lateinit var binding: FragmentDetailThreadBinding
    var forum_Id = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailThreadBinding.inflate(layoutInflater, container, false)

        forum_Id = arguments?.getString("id").toString()
        showData(this).execute()

        binding.btn.setOnClickListener {
            if (binding.etMessage.text == null) {
                helper.toast(requireContext(), "All field must be filled")
                return@setOnClickListener
            }
            sendData(this).execute()
        }

        return binding.root
    }

    class sendData(private var fragment: DetailThreadFragment): AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg p0: String?): String {
            var json = JSONObject()
            json.put("message", fragment.binding.etMessage.text)

            return HttpHandler().request(
                "Forum/${fragment.forum_Id}?message=${fragment.binding.etMessage.text}",
                "POST",
                mySharedPrefrence.getToken(fragment.requireContext()),
            )
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var code = JSONObject(result).getInt("code")

            if (code in 200 until 300) {
                showData(fragment).execute()
            }
        }
    }

    inner class deleteData(private var fragment: DetailThreadFragment, private var reply_id: String): AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg p0: String?): String {
            return HttpHandler().request(
                "Forum/${fragment.forum_Id}/$reply_id",
                "DELETE",
                mySharedPrefrence.getToken(fragment.requireContext())
            )
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var code = JSONObject(result).getInt("code")
            helper.log(code.toString())

            if (code in 200 until 300) {
                showData(fragment).execute()
            }
        }
    }

    class showData(private var fragment: DetailThreadFragment): AsyncTask<Void, Void, Void>() {
        var threadList: MutableList<Forum> = arrayListOf()
        override fun doInBackground(vararg p0: Void?): Void? {
            try {
                var jsonUrl = HttpHandler().request(
                    "Forum/${fragment.forum_Id}",
                    token = mySharedPrefrence.getToken(fragment.requireContext())
                )

                var code = JSONObject(jsonUrl).getInt("code")
                var body = JSONObject(jsonUrl).getString("body")
//                helper.log(code.toString())

                if (code in 200 until 300) {
                    var res = JSONObject(body)
                    var jsonArray = res.getJSONArray("replies")
                    for (i in 0 until jsonArray.length()) {
                        var resReplies = jsonArray.getJSONObject(i)
                        var resCreate = resReplies.getJSONObject("createdBy")

                        threadList.add(Forum(
                            resReplies.getString("id"),
                            message = resReplies.getString("message"),
                            createdAt = resReplies.getString("createdAt"),
                            name = resCreate.getString("name"),
                            email = resCreate.getString("email"),
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
                rv.adapter = DetailThreadAdapter(fragment, threadList)
                rv.layoutManager = LinearLayoutManager(fragment.context)
            }
        }
    }

}