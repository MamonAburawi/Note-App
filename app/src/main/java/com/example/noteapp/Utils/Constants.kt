package com.example.noteapp.Utils

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("StaticFieldLeak")
object Constants {


    private val database = FirebaseFirestore.getInstance()
//    var notId =""
//    val notePath = database.collection("Notes").document(notId)
    val allNotePath = database.collection("Notes")
}