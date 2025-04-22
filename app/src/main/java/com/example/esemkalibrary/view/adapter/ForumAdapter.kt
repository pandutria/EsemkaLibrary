package com.example.esemkalibrary.view.adapter

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.esemkalibrary.databinding.ItemForumBinding
import com.example.esemkalibrary.model.Forum
import com.example.esemkalibrary.network.HttpHandler
import com.example.esemkalibrary.util.helper
import com.example.esemkalibrary.util.mySharedPrefrence
import com.example.esemkalibrary.view.ui.activity.MainActivity
import com.example.esemkalibrary.view.ui.fragment.AddThreadFragment
import com.example.esemkalibrary.view.ui.fragment.ForumFragment
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

class ForumAdapter(private var fragment: ForumFragment, private var forumList: List<Forum>): RecyclerView.Adapter<ForumAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemForumBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = ItemForumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var forum = forumList[position]
        holder.binding.apply {
            tvName.text = forum.name
            tvSubject.text = forum.subject

            var input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            var output = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

            var parse = input.parse(forum.lastestReply)
            var format  = output.format(parse)

            tvLastestReply.text = "Lastest: ${format}"

            if (helper.email == forum.email) {
                btn.visibility = View.VISIBLE
            } else {
                btn.visibility = View.GONE
            }

            btn.setOnClickListener {
                fragment.deleteData(fragment, forum.id!!).execute()
            }

//            holder.itemView.setOnClickListener {
//                var context = holder.itemView.context
//                if (context is MainActivity) {
//                    var fragment = AddThreadFragment()
//                    var bundle = Bundle()
//                    bundle.putString("id", forum.id)
//                    fragment.arguments = bundle
//
//                    context.showFragment(fragment)
//                }
//            }

        }
    }

    override fun getItemCount(): Int {
        return forumList.size
    }

}