package com.example.noteapp.model

import com.google.firebase.Timestamp

data class NoteData(
    val id: String,
    val title: String,
    val description: String,
    val imageUri: String,
    val noteTime: Timestamp
){
    constructor():this("","","","", Timestamp.now())
}
