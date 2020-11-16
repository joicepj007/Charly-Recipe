package com.android.charly.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.charly.persistence.Note
import com.android.charly.repository.NoteRepository
import javax.inject.Inject

class NoteViewModel @Inject constructor(
    val noteRepository: NoteRepository
) : ViewModel() {


    //Database Operations in view model


    // Method #1
    fun insert(note: Note) {
        return noteRepository.insert(note)
    }

    // Method #2
    fun delete(note: Note) {
        noteRepository.delete(note)
    }

    // Method #3
    fun getAllNotes(): LiveData<List<Note>> {
        Log.e("DEBUG", "View model getallnotes")
        return noteRepository.getAllNotes()
    }


}