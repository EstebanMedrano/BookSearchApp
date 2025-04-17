package com.example.booksearchapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(book: BookEntity)

    @Delete
    suspend fun delete(book: BookEntity)

    @Query("SELECT * FROM favorite_books")
    fun getAllFavorites(): Flow<List<BookEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_books WHERE title = :title AND authors = :authors AND year = :year)")
    suspend fun isBookFavorite(title: String?, authors: String?, year: String?): Boolean
}