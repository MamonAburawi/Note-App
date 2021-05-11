package com.example.noteapp.screens.details

import android.content.Context
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.noteapp.Utils.Constants
import com.example.noteapp.model.NoteData
import kotlinx.coroutines.*

class DetailsViewModel(val data: NoteData) : ViewModel() {


    private val _noteData = MutableLiveData<NoteData>()
    val noteData: LiveData<NoteData> = _noteData

    private val viewModelScope = CoroutineScope(Dispatchers.Main + Job())

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress


    init {
        _noteData.value = data
        _progress.value = true
    }


    fun loadImage(image: ImageView ,context: Context){
        _progress.value = true
        viewModelScope.launch {
            Constants.filePath(data.imageId).downloadUrl.addOnSuccessListener {
                Glide.with(context)
                        .load(it)
                        .centerCrop()
                        .into(image)
                _progress.value = false
            }
        }
    }



    override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
    }


}

