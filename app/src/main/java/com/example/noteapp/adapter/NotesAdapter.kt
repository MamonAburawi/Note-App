package com.example.noteapp.adapter


import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.noteapp.R
import com.example.noteapp.Utils.NoteDiffCallback
import com.example.noteapp.Utils.setDateFormat
import com.example.noteapp.databinding.SingleNoteBinding
import com.example.noteapp.model.NoteData
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotesAdapter(private val context: Context) : ListAdapter<NoteData, NotesAdapter.ViewHolder>(NoteDiffCallback()) {

    val diff = AsyncListDiffer(this, NoteDiffCallback())

    class ViewHolder private constructor(private val binding: SingleNoteBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: NoteData, context: Context) {
            binding.apply {
                TextViewTitle.text = data.title
                TextViewTime.text = setDateFormat(data.noteTime)

                loadImages(data.imageId) { uri ->
                    Glide.with(context)
                            .load(uri)
                            .placeholder(R.drawable.ic_no_photography)
                            .into(imageView)
                }

                singleItem.setOnClickListener {

                }
            }


        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.single_note, parent, false))
            }
        }

        private fun loadImages(imageId: String, onComplete: (Uri) -> Unit) {
            GlobalScope.launch(Dispatchers.IO) {
                FirebaseStorage.getInstance().reference.child("Photos/$imageId")
                        .downloadUrl.addOnSuccessListener {
                            onComplete(it)
                        }
                        .addOnFailureListener {
                            Log.i("Images", it.message.toString())
                        }
            }
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = diff.currentList[position]
        holder.onBind(data, context)
    }

    override fun getItemCount(): Int = diff.currentList.size


}

