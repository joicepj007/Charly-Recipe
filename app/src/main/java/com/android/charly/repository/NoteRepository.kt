package com.android.charly.repository

import androidx.lifecycle.LiveData
import com.android.charly.persistence.Note
import com.android.charly.persistence.NoteDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDao: NoteDao) {

    // Method #1
    //function to insert note in database
    fun insert(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.insert(note)
        }
    }

    // Method #2
    //function to delete note in database
    fun delete(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.delete(note)
        }
    }

    // Method #3
    //function to get all notes in database
    fun getAllNotes(): LiveData<List<Note>>{
        return noteDao.getAllNotes()
    }
}