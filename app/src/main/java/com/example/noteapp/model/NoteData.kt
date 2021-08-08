package com.example.noteapp.model


import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteData(
    val id: String,
    val title: String,
    val description: String,
    val imageId: String,
    val noteTime: Timestamp
):Parcelable{
    constructor():this("","","","", Timestamp.now())
}
