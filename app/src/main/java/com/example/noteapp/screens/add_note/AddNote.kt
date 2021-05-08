package com.example.noteapp.screens.add_note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.Utils.generateUUID
import com.example.noteapp.Utils.getCurrentTime
import com.example.noteapp.Utils.hideKeyboard
import com.example.noteapp.databinding.AddNoteBinding
import com.example.noteapp.model.NoteData

@Suppress("DEPRECATION")
class AddNote : Fragment() {

    private lateinit var binding: AddNoteBinding
    private lateinit var viewModel: AddNoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.add_note,container,false)
        viewModel = ViewModelProviders.of(this).get(AddNoteViewModel::class.java)


        // navigation to main screen
        viewModel.navigationToMainScreen.observe(viewLifecycleOwner, Observer {
            if (it == true){
                findNavController().navigate(R.id.action_addNote_to_mainScreen)
                viewModel.navigationToMainScreenDone()
            }
        })


        binding.ButtonAdd.setOnClickListener {
            val uuid = generateUUID()
            val title = binding.EditTextNoteTitle.text.trim().toString()
            val description = binding.EditTextNoteDescription.text.trim().toString()

            addNote(uuid, title, description)
        }


        return binding.root
    }


    private fun addNote(uuid: String ,title: String , description: String){
        if (title.isNotEmpty() && description.isNotEmpty()){
            val noteData = NoteData(uuid,title,description,"", getCurrentTime())
            viewModel.addNote(noteData)
            viewModel.navigationToMainScreen()
            requireActivity().hideKeyboard(binding.ButtonAdd)
            Toast.makeText(activity,"Note is added",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(activity,"Please fill information",Toast.LENGTH_SHORT).show()
        }
    }



}