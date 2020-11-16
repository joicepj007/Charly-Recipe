package com.android.charly.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.charly.utils.StringListConverter


// - Database Class
@Database(entities = [Note::class],version = 1)
@TypeConverters(StringListConverter::class)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}