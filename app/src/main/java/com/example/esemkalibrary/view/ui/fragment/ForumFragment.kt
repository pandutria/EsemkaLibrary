package com.example.esemkalibrary.view.ui.fragment

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.esemkalibrary.R
import com.example.esemkalibrary.databinding.FragmentForumBinding
import com.example.esemkalibrary.model.Forum
import com.example.esemkalibrary.network.HttpHandler
import com.example.esemkalibrary.util.helper
import com.example.esemkalibrary.util.mySharedPrefrence
import com.example.esemkalibrary.view.adapter.ForumAdapter
import com.example.esemkalibrary.view.ui.activity.MainActivity
import org.json.JSONArray
import org.json.JSONObject

class ForumFragment : Fragment() {
    lateinit var binding: FragmentForumBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForumBinding.inflate(layoutInflater, container, false)

        showData(this).execute()

        binding.btn.setOnClickListener {
            var context = context
            if (context is MainActivity) {
                context.showFragment(AddThreadFragment())
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        showData(this).execute()
    }

    inner class deleteData (private var fragment: ForumFragment, private var id: String): AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg p0: String?): String {
            return HttpHandler().request(
                "Forum/$id",
                "DELETE",
                mySharedPrefrence.getToken(fragment.requireContext())
            )
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var code = JSONObject(result).getInt("code")
            var body = JSONObject(result).getString("body")

            if (code in 200 until 300) {
               showData(fragment).execute()
            }
        }
    }

    class showData(private var fragment: ForumFragment): AsyncTask<Void,Void, Void>() {
        var forumList: MutableList<Forum> = arrayListOf()
        override fun doInBackground(vararg p0: Void?): Void? {
            try {
                var jsonUrl = HttpHandler().request(
                    "Forum",
                    token = mySharedPrefrence.getToken(fragment.requireContext())
                )

                var code = JSONObject(jsonUrl).getInt("code")
                var body = JSONObject(jsonUrl).getString("body")

                if (code in 200 until 300) {
                    var jsonArray = JSONArray(body)

                    for (i in 0 until jsonArray.length()) {
                        var forum = jsonArray.getJSONObject(i)

                        var creator = forum.getJSONObject("createdBy")

                        forumList.add(Forum(
                            forum.getString("id"),
                            forum.getString("subject"),
                            forum.getString("createdAt"),
                            forum.getString("lastestReply"),
                            creator.getString("name"),
                            creator.getString("email"),
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
                rv.adapter = ForumAdapter(fragment, forumList)
                rv.layoutManager = LinearLayoutManager(fragment.context)
            }
        }
    }

}