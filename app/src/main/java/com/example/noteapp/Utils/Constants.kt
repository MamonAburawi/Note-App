package com.example.noteapp.Utils

import android.annotation.SuppressLint
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

@SuppressLint("StaticFieldLeak")
object Constants {


    private val database = FirebaseFirestore.getInstance()

    val allNotePath = database.collection("Notes")

//    fun filePath(x : String): StorageReference {
//        return FirebaseStorage.getInstance().reference.child("Images/$x")
//    }

//    val filePath = FirebaseStorage.getInstance().reference.child("Images")


}