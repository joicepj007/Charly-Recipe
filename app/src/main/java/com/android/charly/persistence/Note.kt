package com.android.charly.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


// - Entity class for Recipe note table
@Entity(tableName = "tbl_note")
data class Note(

        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0,

        @ColumnInfo(name = "title")
        var title: String,

        @ColumnInfo(name = "description")
        var description: String,

        @ColumnInfo(name = "image")
        var image: MutableList<String?>?

)
