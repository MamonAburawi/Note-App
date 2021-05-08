package com.example.noteapp.database

import com.example.noteapp.Utils.Constants
import com.example.noteapp.model.NoteData

class DataBase() {



    suspend fun addData(data:NoteData){
        val notePath = Constants.allNotePath.document(data.id)
        notePath.set(data)
    }


     suspend fun deleteData(id:String){
        Constants.allNotePath.document(id).delete()
    }

     fun getAllNotes() = Constants.allNotePath.get()




}