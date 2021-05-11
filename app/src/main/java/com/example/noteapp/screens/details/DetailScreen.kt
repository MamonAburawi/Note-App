package com.example.noteapp.screens.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.noteapp.R
import com.example.noteapp.databinding.DeltailScreenBinding


class DetailScreen : Fragment() {

    private lateinit var binding: DeltailScreenBinding
    private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.deltail_screen,container,false)

        val noteData =  navArgs<DetailScreenArgs>().value.noteData
        viewModel = ViewModelProvider(this,DetailScreenViewModelFactory(noteData)).get(DetailsViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.progress.observe(viewLifecycleOwner, Observer { progress->
            if(progress == true){
                binding.imageProgress.visibility = View.VISIBLE
                binding.ImageViewNote.visibility = View.GONE
            }else{
                binding.imageProgress.visibility = View.GONE
                binding.ImageViewNote.visibility = View.VISIBLE
            }
        })


        // noteData
        viewModel.noteData.observe(viewLifecycleOwner, Observer { noteData ->
            if(noteData != null){
                if (noteData.imageUri.isNotEmpty()){
                    viewModel.loadImage(binding.ImageViewNote,requireActivity())
                }else{
                    binding.ImageViewNote.setImageResource(R.drawable.ic_no_photography)
                    binding.imageProgress.visibility = View.GONE
                    binding.ImageViewNote.visibility = View.VISIBLE
                }
            }
        })



        return binding.root
    }




}