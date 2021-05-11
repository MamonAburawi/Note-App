package com.example.noteapp.database

import android.net.Uri
import com.example.noteapp.Utils.Constants
import com.example.noteapp.model.NoteData
import com.google.android.gms.tasks.Task


class DataBase() {

    suspend fun addData(data:NoteData): Task<Void> {
        val notePath = Constants.allNotePath.document(data.id)
        return notePath.set(data)
    }

    suspend fun deleteData(id:String){
        Constants.allNotePath.document(id).delete()
    }

    suspend fun uploadFile(imageId: String,imageUri: Uri) {
        Constants.filePath(imageId).putFile(imageUri)
    }

     fun getFile(imageUri: String): Task<Uri> {
        return Constants.filePath(imageUri).downloadUrl
    }

     fun getAllNotes() = Constants.allNotePath.get()




}