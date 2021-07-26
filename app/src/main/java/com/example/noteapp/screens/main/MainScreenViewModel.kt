package com.example.noteapp.screens.main


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteapp.Utils.Constants
import com.example.noteapp.Utils.Sort
import com.example.noteapp.adapter.NotesAdapter
import com.example.noteapp.database.DataBase
import com.example.noteapp.model.NoteData
import com.google.firebase.firestore.Query
import kotlinx.coroutines.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class MainScreenViewModel(): ViewModel() {

    private val database = DataBase()

    private val viewModelScope = CoroutineScope(Dispatchers.Main + Job())


    private val _noteList = MutableLiveData<List<NoteData>>()
    val noteList: LiveData<List<NoteData>> = _noteList

    private val _navigateToAddNoteScreen = MutableLiveData<Boolean>()
    val navigateToAddNoteScreen: LiveData<Boolean> = _navigateToAddNoteScreen

    private val _results = MutableLiveData<String>()
    val results: LiveData<String> = _results

    private val _navigateToDetailScreen  = MutableLiveData<NoteData>()
    val navigateToDetailScreen: LiveData<NoteData> = _navigateToDetailScreen

    private var sort = Sort.Date // by default the sort value will be date


    init {
        _navigateToAddNoteScreen.value = false
        _navigateToDetailScreen.value = null
        updateNotesList()
    }

    fun navigateToAddNoteScreen(){
        _navigateToAddNoteScreen.value = true
    }

    fun navigateToAddNoteScreenDone(){
        _navigateToAddNoteScreen.value = false
    }

    fun navigateToDetailScreenDone(){
        _navigateToDetailScreen.value = null
    }




    private fun updateNotesList() {
        viewModelScope.launch {
            database.getAllNotes().addOnSuccessListener { documents ->
                val notes = documents.toObjects(NoteData::class.java)
                val sortedList = notes.sortedWith(compareBy { it.noteTime.toDate() }).reversed()
                _noteList.value = sortedList
                _results.value = "Results: ${notes.size}"
            }
        }
    }



    fun deleteNote(item: NoteData) {
        viewModelScope.launch {
            database.deleteData(item.id)
        }
    }




    fun sortData(sort: String) {
        viewModelScope.launch {
            Constants.allNotePath
                .orderBy(sort)
                .get().addOnSuccessListener {
                    val notes = it.toObjects(NoteData::class.java)
                    _noteList.value = notes
                }
        }
    }


    fun search(charSequence:String){
        viewModelScope.launch {
            val char = charSequence.trim()
            val query = Constants.allNotePath
                .orderBy("title")
                .startAt(char)
                .endAt(char +"\uf8ff")

            query.get().addOnSuccessListener {
                val notes = it.toObjects(NoteData::class.java)
                _noteList.value = notes
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }


}