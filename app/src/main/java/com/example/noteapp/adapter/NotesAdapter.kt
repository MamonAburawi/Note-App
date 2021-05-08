package com.example.noteapp.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.Utils.NoteDiffCallback
import com.example.noteapp.Utils.setDateFormat
import com.example.noteapp.databinding.SingleNoteBinding
import com.example.noteapp.model.NoteData
import com.example.noteapp.screens.main.MainScreenViewModel

class NotesAdapter(private val notesList: List<NoteData>,
                   private val viewModel: MainScreenViewModel): ListAdapter<NoteData,NotesAdapter.ViewHolder>(NoteDiffCallback()) {


    class ViewHolder(private val binding: SingleNoteBinding)
        :RecyclerView.ViewHolder(binding.root) {

        fun onBind(
            holder: ViewHolder,
            data: NoteData,
            viewModel: MainScreenViewModel,
            notesList: List<NoteData>,
            position: Int
        ){
            holder.binding.TextViewTitle.text = data.title
            holder.binding.TextViewTime.text = setDateFormat(data.noteTime)

            // Button Delete
            holder.binding.ButtonDeleteNote.setOnClickListener {
                viewModel.showDeleteDialog(data.id)
            }



        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.single_note,parent,false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = notesList[position]
        holder.onBind(holder,data,viewModel , notesList,position)
    }

    override fun getItemCount(): Int  = notesList.size




}

