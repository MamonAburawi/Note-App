package com.example.noteapp.screens.add_note

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
    private var uriPath = Uri.EMPTY

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

            if(uriPath != null){
                addNote(uuid, title, description , uriPath)
            }else{
                addNote(uuid, title , description , Uri.EMPTY)
            }

        }


        binding.ButtonSelectImage.setOnClickListener{
            selectImages()
        }


        return binding.root
    }


    private fun addNote(uuid: String ,title: String , description: String ,image: Uri){
        if (title.isNotEmpty() && description.isNotEmpty()){
            val noteData = NoteData(
                uuid,
                title,
                description,
                generateUUID(),
                getCurrentTime())
            viewModel.addNote(noteData, image)
            viewModel.navigationToMainScreen()
            requireActivity().hideKeyboard(binding.ButtonAdd)
            Toast.makeText(activity,"Note is added",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(activity,"Please fill information",Toast.LENGTH_SHORT).show()
        }
    }



    private fun selectImages(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Choose you images"),1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode  == Activity.RESULT_OK &&  data != null){
            uriPath = data.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,uriPath)
            binding.ImageViewNote.setImageBitmap(bitmap).toString()
        }
    }

}