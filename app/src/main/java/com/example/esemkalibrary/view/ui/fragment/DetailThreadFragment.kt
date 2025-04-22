package com.example.esemkalibrary.view.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.esemkalibrary.R
import com.example.esemkalibrary.databinding.FragmentDetailThreadBinding

class DetailThreadFragment : Fragment() {
    lateinit var binding: FragmentDetailThreadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailThreadBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    class showData()

}