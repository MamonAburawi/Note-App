package com.example.noteapp.screens.main

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.Utils.Sort
import com.example.noteapp.adapter.NotesAdapter
import com.example.noteapp.databinding.MainScreenBinding


@Suppress("DEPRECATION")
class MainScreen : Fragment() {

    private lateinit var binding: MainScreenBinding
    private lateinit var viewModel: MainScreenViewModel
    private var listSize: Int? = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.main_screen,container,false)
        viewModel = ViewModelProviders.of(this).get(MainScreenViewModel::class.java)



        // navigation to add note screen
        viewModel.navigateToAddNoteScreen.observe(viewLifecycleOwner, Observer {
            if (it == true){
                findNavController().navigate(R.id.action_mainScreen_to_addNote)
                viewModel.navigateToAddNoteScreenDone()
            }
        })

        // navigation to detail screen
        viewModel.navigateToAddNoteScreen.observe(viewLifecycleOwner, Observer {
            if (it == true){
                findNavController().navigate(R.id.action_mainScreen_to_detailScreen)
                viewModel.navigateToDetailScreenDone()
            }
        })

        // results
        viewModel.results.observe(viewLifecycleOwner, Observer { results->
            binding.TextViewResults.text = "Results: $results"
        })

        // noteData
        viewModel.navigateToDetailScreen.observe(viewLifecycleOwner, Observer { noteData->
            if (noteData != null){
                findNavController().navigate(MainScreenDirections.actionMainScreenToDetailScreen(noteData))
                viewModel.navigateToDetailScreenDone()
            }
        })

        // note list
        viewModel.noteList.observe(viewLifecycleOwner, Observer { noteList ->
            listSize = noteList.size
            binding.recyclerView.adapter = NotesAdapter(noteList,viewModel)
            if (noteList.isEmpty()){
                binding.recyclerView.visibility = View.GONE
                binding.TextViewNoData.visibility = View.VISIBLE
            }else{
                binding.recyclerView.visibility = View.VISIBLE
                binding.TextViewNoData.visibility = View.GONE
            }
        })


        // progress
        viewModel.progressStatus.observe(viewLifecycleOwner, Observer { status->
            if(status == true){
                binding.Progress.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }else{
                binding.Progress.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        })



        /** the list will be updated with any change in size.  **/

        // onChange List Size
        viewModel.onChangeListSize.observe(viewLifecycleOwner, Observer { size->
            if (size != listSize){
                viewModel.updateNotesList()
                Log.i("size","the size is not the same!")
            }else{
                Log.i("size","the size is the same!")
            }
        })

        // Button add note
        binding.ButtonAddNote.setOnClickListener{
          viewModel.navigateToAddNoteScreen()
        }

        // button filter by date
        binding.ButtonSortByDate.setOnClickListener {
            viewModel.sort(Sort.Date)
        }

        // button filter by name
        binding.ButtonSortByName.setOnClickListener {
            viewModel.sort(Sort.Name)
        }

        // search edit text
        binding.EditTextSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.search(charSequence.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        // delete dialog status
        viewModel.deleteDialogStatus.observe(viewLifecycleOwner, Observer { status->
            if (status == true){
                val builder = AlertDialog.Builder(context).apply {
                    setTitle("Delete item !")
                    setMessage("Do you want to delete this item?")
                    setIcon(R.drawable.ic_delete)
                    setCancelable(false)
                    setPositiveButton("yes"){_,_ ->
                        val id = viewModel.noteId
                        if (id.isNotEmpty()){
                            viewModel.deleteNote(id)
                        }else{
                            Log.i("dialog","note id is empty !")
                        }
                    }
                    setNegativeButton("No"){_,_ -> }
                    create()
                }
                builder.show()
                viewModel.hideDeleteDialog()
            }
        })



        return binding.root
    }





}