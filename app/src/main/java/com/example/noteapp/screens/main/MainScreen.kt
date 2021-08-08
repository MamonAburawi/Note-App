package com.example.noteapp.screens.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.example.noteapp.R
import com.example.noteapp.Utils.Sort
import com.example.noteapp.adapter.NotesAdapter
import com.example.noteapp.databinding.MainScreenBinding
import com.example.noteapp.model.NoteData
import com.google.android.material.snackbar.Snackbar


@Suppress("DEPRECATION")
class MainScreen : Fragment() {

    private lateinit var binding: MainScreenBinding
    private lateinit var viewModel: MainScreenViewModel
    private lateinit var noteAdapter: NotesAdapter
    private var mList: List<NoteData>? = null




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.main_screen,container,false)
        viewModel = ViewModelProviders.of(this).get(MainScreenViewModel::class.java)

        binding.lifecycleOwner = this
        noteAdapter = NotesAdapter(requireActivity())
        binding.viewModel = viewModel

        binding.apply {

            recyclerView.adapter = noteAdapter

            // search edit text
            EditTextSearch.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel!!.search(charSequence.toString())
                }

                override fun afterTextChanged(p0: Editable?) {}
            })
        }



        swipeToDelete()

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


        // note list
        viewModel.noteList.observe(viewLifecycleOwner, Observer { noteList ->

            mList = noteList.sortedBy { it.noteTime }

            if (noteList.isNotEmpty()){
//                noteAdapter.diff.submitList(mList)
                initNotesData(mList!!)
                binding.recyclerView.visibility = View.VISIBLE
                binding.TextViewNoData.visibility = View.GONE

            }else{
                binding.recyclerView.visibility = View.GONE
                binding.TextViewNoData.visibility = View.VISIBLE
            }
        })


        // Button add note
        binding.ButtonAddNote.setOnClickListener{
          viewModel.navigateToAddNoteScreen()
        }

        // button filter by date
        binding.ButtonSortByDate.setOnClickListener {
           initNotesData( mList!!.sortedBy { it.noteTime })
        }

        // button filter by name
        binding.ButtonSortByName.setOnClickListener {
            initNotesData( mList!!.sortedBy { it.title })
        }




        return binding.root
    }




    private fun swipeToDelete() {
        ItemTouchHelper(object : SimpleCallback(
            UP or DOWN,
            RIGHT or LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
                target: ViewHolder
            ) = true

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                val noteList = mList!!.toMutableList()
                val position = viewHolder.adapterPosition
                val item = noteAdapter.diff.currentList[position]
//                newList.remove(item)
//                noteAdapter.notifyItemRemoved(position)
                viewModel.deleteNote(item)

                Snackbar.make(binding.itemRoot,"Item $position Deleted",Snackbar.LENGTH_LONG).apply {
                    duration = 5000

                    setAction("Undo"){
                        // add the note to database
                        viewModel.addNote(item)
                    }
                }.show()
            }

        }).attachToRecyclerView(binding.recyclerView)
    }


    private fun initNotesData(list: List<NoteData>){
        noteAdapter.diff.submitList(list)
    }

}