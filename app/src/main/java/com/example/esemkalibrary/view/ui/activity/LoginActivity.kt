package com.example.esemkalibrary.view.ui.activity

import android.os.AsyncTask
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.esemkalibrary.R
import com.example.esemkalibrary.databinding.ActivityLoginBinding
import com.example.esemkalibrary.network.HttpHandler
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    class login(private var activity: LoginActivity): AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg p0: String?): String {
            activity.binding.apply {
                var json = JSONObject()
                json.put("email", etEmail.text)
                json.put("password", etPassword.text)

                return HttpHandler().request(
                    "Auth",
                    "POST",
                    rBody = json.toString()
                )
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var code = JSONObject(result).getInt("code")
            var body = JSONObject(result).getString("body")

            if (code == 200) {
                var res = JSONObject(body)

            }
        }
    }
}