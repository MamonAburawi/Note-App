package com.example.noteapp.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.Utils.NoteDiffCallback
import com.example.noteapp.Utils.setDateFormat
import com.example.noteapp.databinding.SingleNoteBinding
import com.example.noteapp.model.NoteData
import com.example.noteapp.screens.main.MainScreenViewModel

class NotesAdapter(): ListAdapter<NoteData,NotesAdapter.ViewHolder>(NoteDiffCallback()) {

    val diff = AsyncListDiffer(this,NoteDiffCallback())


    class ViewHolder private constructor(private val binding: SingleNoteBinding)
        :RecyclerView.ViewHolder(binding.root) {
        fun onBind( data: NoteData){
            binding.TextViewTitle.text = data.title
            binding.TextViewTime.text = setDateFormat(data.noteTime)

            binding.singleItem.setOnClickListener {

            }

        }

        companion object {
            fun from(parent: ViewGroup):ViewHolder{
                return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.single_note,parent,false))
            }
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = diff.currentList[position]
        holder.onBind(data)
    }

    override fun getItemCount(): Int  = diff.currentList.size




}

