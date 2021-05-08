package com.example.noteapp.screens.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.noteapp.R
import com.example.noteapp.databinding.DeltailScreenBinding

class DetailScreen : Fragment() {

    private lateinit var binding: DeltailScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
         binding = DataBindingUtil.inflate(inflater, R.layout.deltail_screen,container,false)

        return binding.root
    }

}