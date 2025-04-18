package com.example.esemkalibrary.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment

object helper {
    var url = "http://10.0.2.2:5000/Api/"

    fun toast(context: Context, string: String) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show()
    }

    fun log(string: String) {
        Log.d("DataApi", "Eror : $string")
    }
}