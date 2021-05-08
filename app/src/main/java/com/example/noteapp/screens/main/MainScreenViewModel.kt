package com.example.noteapp.screens.main


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteapp.Utils.Sort
import com.example.noteapp.database.DataBase
import com.example.noteapp.model.NoteData
import com.google.firebase.Timestamp
import kotlinx.coroutines.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class MainScreenViewModel(): ViewModel() {

    private val database = DataBase()

    private val viewModelScope = CoroutineScope(Dispatchers.Main + Job())

    private var mList = ArrayList<NoteData>()

    private val _noteList = MutableLiveData<List<NoteData>>()
    val noteList: LiveData<List<NoteData>> = _noteList

    private val _navigateToAddNoteScreen = MutableLiveData<Boolean>()
    val navigateToDetailScreen: LiveData<Boolean> = _navigateToAddNoteScreen

    private val _progressStatus = MutableLiveData<Boolean>()
    val progressStatus: LiveData<Boolean> = _progressStatus

    private val _onChangeListSize = MutableLiveData<Int>()
    val onChangeListSize: LiveData<Int> = _onChangeListSize

    private val _deleteDialogStatus = MutableLiveData<Boolean>()
    val deleteDialogStatus: LiveData<Boolean> = _deleteDialogStatus

    private val _results = MutableLiveData<String>()
    val results: LiveData<String> = _results

    var noteId = ""
    var possition = 0

    private var sort = Sort.Date // by default the sort value will be date


    init {
        _navigateToAddNoteScreen.value = false
        _navigateToAddNoteScreen.value = false
        updateNotesList()
    }

    fun navigateToAddNoteScreen(){
        _navigateToAddNoteScreen.value = true
    }

    fun navigateToAddNoteScreenDone(){
        _navigateToAddNoteScreen.value = false
    }

    fun updateNotesList() {
        _progressStatus.value = true
        viewModelScope.launch {
            mList.clear()
            database.getAllNotes().addOnSuccessListener { documents ->
                documents.forEach {
                    val id = it["id"].toString()
                    val title = it["title"].toString()
                    val description = it["description"].toString()
                    val imageUri = it["imageUri"].toString()
                    val noteTime = it["noteTime"] as Timestamp
                    mList.add(NoteData(id, title, description, imageUri, noteTime))
                }
                _noteList.value = mList.sortedWith(compareBy { it.noteTime.toDate() }).reversed()
                _progressStatus.value = false
                _onChangeListSize.value = mList.size
                _results.value = mList.size.toString()
            }
        }
    }



    fun deleteNote(id: String) {
        viewModelScope.launch {
            database.deleteData(id)
            _onChangeListSize.value = mList.size - 1
        }
    }

    fun showDeleteDialog(id: String) {
        noteId = id
        _deleteDialogStatus.value = true
    }

    fun hideDeleteDialog(){
        _deleteDialogStatus.value = false
    }


   fun sort(s:Sort){
       viewModelScope.launch {
           sort = s
           when(s){
               Sort.Date->{
                   _noteList.value = mList.sortedWith(compareBy { it.noteTime.toDate() }).reversed() // sort data from old to new date
               }
               Sort.Name->{
                   _noteList.value = mList.sortedWith(compareBy{ it.title }) }
               }
           }
       }


    fun search(charSequence:String){
        val filterPattern = charSequence.trim()
        if (filterPattern.isNotEmpty()){ // filter data
            val filterList = mList.filter { it.title.contains(filterPattern) } as ArrayList<NoteData>
            _noteList.value = filterList
            _results.value = filterList.size.toString()
        }else{ // all data
             sort(sort)
            _results.value = mList.size.toString()
        }
    }



    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }


}