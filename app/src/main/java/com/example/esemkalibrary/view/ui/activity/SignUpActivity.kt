package com.example.esemkalibrary.view.ui.activity

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.esemkalibrary.R
import com.example.esemkalibrary.databinding.ActivitySignUpBinding
import com.example.esemkalibrary.network.HttpHandler
import com.example.esemkalibrary.util.helper
import com.example.esemkalibrary.util.mySharedPrefrence
import com.example.esemkalibrary.view.ui.activity.LoginActivity.login
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            binding.apply {
                if (etEmail.text.toString() == null || etPassword.text.toString() == null || etName.text.toString() == null) {
                    helper.toast(this@SignUpActivity, "All fields must be filled")
                    return@setOnClickListener
                }

                if (!etPassword.text.any { c -> c.isDigit() } || !etPassword.text.any { c -> c.isLetter() }
                    || !etPassword.text.any { c -> !c.isLetterOrDigit() }) {
                    helper.toast(this@SignUpActivity, "Password must have a digit, a letter and a symbol")
                    return@setOnClickListener
                }

                if (!etEmail.text.contains("@") || !etEmail.text.contains(".com")) {
                    helper.toast(this@SignUpActivity, "Email must be valid format")
                    return@setOnClickListener
                }

                if (etPassword.text.length < 8) {
                    helper.toast(this@SignUpActivity, "Password must be minimal 8 characters")
                    return@setOnClickListener
                }

                if (etPassword.text.toString() != etConfirm.text.toString()) {
                    helper.toast(this@SignUpActivity, "Password and confirm password must be same")
                    return@setOnClickListener
                }
            }

            register(this).execute()
        }
    }

    class register(private var activity: SignUpActivity): AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg p0: String?): String {
            activity.binding.apply {
                var json = JSONObject()
                json.put("name", etName.text)
                json.put("password", etPassword.text)
                json.put("email", etEmail.text)

                return HttpHandler().request(
                    "Users",
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

                    if (code in 200 until 300) {
                        activity.startActivity(Intent(activity, LoginActivity::class.java))
                        activity.finish()
//                        helper.log(res.getString("token"))
                    } else {
                        helper.toast(activity, JSONObject(body).getString(""))
                    }

                } catch (e: Exception) {
                    helper.log(e.message!!)
                }

            }
        }
    }
}