package com.example.esemkalibrary.util

import android.content.Context

object mySharedPrefrence {
    var token = "token"
    var sharedKey = "sharedKey"

    fun saveToken(context: Context, tokenKey: String) {
        var shared = context.getSharedPreferences(sharedKey, Context.MODE_PRIVATE)
        with(shared.edit()) {
            putString(token, tokenKey)
            apply()
        }
    }

    fun getToken(context: Context): String? {
        var shared = context.getSharedPreferences(sharedKey, Context.MODE_PRIVATE)
        return shared.getString(token, null)
    }

    fun deleteToken(context: Context) {
        var shared = context.getSharedPreferences(sharedKey, Context.MODE_PRIVATE)
        with(shared.edit()) {
            remove(token)
            apply()
        }
    }
}