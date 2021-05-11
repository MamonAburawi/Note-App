package com.example.noteapp.Utils


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.DiffUtil
import com.example.noteapp.model.NoteData
import com.google.firebase.Timestamp
import java.util.*

enum class Sort(){
    Name,
    Date
}

class NoteDiffCallback : DiffUtil.ItemCallback<NoteData>() {
    override fun areItemsTheSame(oldItem: NoteData, newItem: NoteData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NoteData, newItem: NoteData): Boolean {
        return oldItem == newItem
    }
}

fun generateUUID() = UUID.randomUUID().toString()

fun getCurrentTime() = Timestamp.now()

fun setDateFormat(date: Timestamp) = DateFormat.format("yyyy/MM/dd  h:mm a", date.toDate()).toString()


fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}