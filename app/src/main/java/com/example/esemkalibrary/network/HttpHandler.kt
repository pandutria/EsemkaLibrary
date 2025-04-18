package com.example.esemkalibrary.network

import android.util.Log
import com.example.esemkalibrary.util.helper
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class HttpHandler {
    fun request(
        endpoint: String,
        method: String? = "GET",
        token: String? = null,
        rBody: String? = null
    ) : String {
        var response = JSONObject()

        try {
            var url = URL(helper.url + endpoint)
            var conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = method
            conn.setRequestProperty("Content-Type", "application/json")

            if (token != null) {
                conn.setRequestProperty("Authorization", "Bearer $token")
            }

            if (rBody != null) {
                conn.doOutput = true
                conn.outputStream.use { it.write(rBody.toByteArray()) }
            }

            var code = conn.responseCode
            var body = try {
                conn.inputStream.bufferedReader().use { it.readText() }
            } catch (e: Exception) {
                conn.errorStream.bufferedReader().use { it.readText() }
            }

            response.put("code", code)
            response.put("body", body)
        } catch (e: Exception) {
            Log.d("HttpHandler", "Eror ${e.message}")
        }

        return response.toString()
    }
}