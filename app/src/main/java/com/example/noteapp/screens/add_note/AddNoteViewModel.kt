package com.example.noteapp.screens.add_note

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteapp.database.DataBase
import com.example.noteapp.model.NoteData
import kotlinx.coroutines.*


class AddNoteViewModel(): ViewModel() {

    private val database = DataBase()

    private val viewModelScope = CoroutineScope(Dispatchers.IO + Job())

    private val _navigationToMainScreen = MutableLiveData<Boolean>()
    val navigationToMainScreen: LiveData<Boolean> = _navigationToMainScreen


    init {
        _navigationToMainScreen.value = false
    }

    fun navigationToMainScreen(){
        _navigationToMainScreen.value = true
    }

    fun navigationToMainScreenDone(){
        _navigationToMainScreen.value = false
    }

    fun addNote(noteData: NoteData, image: Uri){
        viewModelScope.launch {
            database.addData(noteData)
                .addOnSuccessListener {  }
                .addOnFailureListener {
                    Log.e("send",it.message.toString())
                }
            uploadFile(noteData.imageId,image)
        }
    }

    private fun uploadFile(imageId:String,image: Uri){
        viewModelScope.launch {
            database.uploadFile(imageId,image)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}