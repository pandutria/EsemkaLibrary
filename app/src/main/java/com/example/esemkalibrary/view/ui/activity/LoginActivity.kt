package com.example.esemkalibrary.view.ui.activity

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.esemkalibrary.R
import com.example.esemkalibrary.databinding.ActivityLoginBinding
import com.example.esemkalibrary.network.HttpHandler
import com.example.esemkalibrary.util.helper
import com.example.esemkalibrary.util.mySharedPrefrence
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            etEmail.setText("rlochran4@hatena.ne.jp")
            etPassword.setText("YLhGt2JDH01N")
        }

        binding.btnLogin.setOnClickListener {
            binding.apply {
                if (etEmail.text.toString() == null || etPassword.text.toString() == null) {
                    helper.toast(this@LoginActivity, "All fields must be filled")
                    return@setOnClickListener
                }
            }

            login(this).execute()
        }

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
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
            if (result!!.isNotEmpty()) {
                try {
                    var code = JSONObject(result).getInt("code")
                    var body = JSONObject(result).getString("body")
//                    helper.log(code.toString())

                    if (code == 200) {
                        var res = JSONObject(body)
                        mySharedPrefrence.saveToken(activity, res.getString("token"))
                        activity.startActivity(Intent(activity, MainActivity::class.java))
                        activity.finish()
//                        helper.log(res.getString("token"))
                    } else {
                        helper.toast(activity, "Email or Password is invalid")
                    }

                } catch (e: Exception) {
                    helper.log(e.message!!)
                }

            }
        }
    }
}