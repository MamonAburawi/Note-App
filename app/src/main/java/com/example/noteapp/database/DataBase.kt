package com.example.noteapp.database

import android.net.Uri
import android.util.Log
import com.example.noteapp.Utils.Constants
import com.example.noteapp.model.NoteData
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage


class DataBase() {

    suspend fun addData(data:NoteData): Task<Void> {
        val notePath = Constants.allNotePath.document(data.id)
        return notePath.set(data)
    }

    suspend fun deleteData(id:String){
        Constants.allNotePath.document(id).delete()
    }

    suspend fun uploadFile(imageId: String, image: Uri) {
        FirebaseStorage.getInstance()
                .reference
                .child("Photos")
                .child(imageId)
                .putFile(image)
                .addOnSuccessListener {
                    Log.i("DataBase","Image is uploaded \n image Id: $imageId")
                }
    }

     fun getAllNotes() = Constants.allNotePath




}