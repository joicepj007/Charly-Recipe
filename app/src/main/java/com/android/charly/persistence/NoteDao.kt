package com.android.charly.persistence
import androidx.lifecycle.LiveData
import androidx.room.*


// - Interface Dao for Note table sql operations

@Dao
interface NoteDao {

    // Method #1
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note): Long

    // Method #2
    @Query("delete from tbl_note where id = :id")
    fun deleteById(id: Int)

    // Method #3
    @Delete
    fun delete(note: Note)

    // Method #4
    @Query("select * from tbl_note")
    fun getAllNotes():LiveData<List<Note>>
}