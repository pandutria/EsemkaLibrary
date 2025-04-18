package com.example.esemkalibrary.view.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.esemkalibrary.R
import com.example.esemkalibrary.databinding.ActivityMainBinding
import com.example.esemkalibrary.view.ui.fragment.CartFragment
import com.example.esemkalibrary.view.ui.fragment.ForumFragment
import com.example.esemkalibrary.view.ui.fragment.HomeFragment
import com.example.esemkalibrary.view.ui.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showFragment(HomeFragment())

        binding.apply {
            btnHome.setOnClickListener {
                showFragment(HomeFragment())
            }

            btnForum.setOnClickListener {
                showFragment(ForumFragment())
            }

            btnMyCart.setOnClickListener {
                showFragment(CartFragment())
            }

            btnProfile.setOnClickListener {
                showFragment(ProfileFragment())
            }
        }

    }

    fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment)
            .commit()
    }
}