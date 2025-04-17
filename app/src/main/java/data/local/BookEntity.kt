package com.example.booksearchapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val authors: String?,
    val title: String?,
    val year: String?
    // Puedes añadir más campos si necesitas guardar más información localmente
)