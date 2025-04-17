package com.example.booksearchapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.booksearchapp.Book
import com.example.booksearchapp.data.local.AppDatabase
import com.example.booksearchapp.data.local.BookEntity
import com.example.booksearchapp.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class BookViewModel(private val database: AppDatabase) : ViewModel() {
    private val _searchResults = MutableStateFlow<List<Book>>(emptyList())
    val searchResults: StateFlow<List<Book>> = _searchResults

    private val _favoriteBooks = database.bookDao().getAllFavorites()
    val favoriteBooks = _favoriteBooks

    private val apiService = RetrofitInstance.api

    fun searchBooks(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.searchBooks(query)
                _searchResults.value = response.items?.mapNotNull {
                    it.volumeInfo?.let { info ->
                        val year = info.publishedDate?.take(4) // Extraer el año
                        Book(info.authors?.joinToString(", "), info.title, year)
                    }
                } ?: emptyList()
            } catch (e: Exception) {
                // Manejar el error de la búsqueda
                _searchResults.value = emptyList()
                e.printStackTrace()
            }
        }
    }

    fun toggleFavorite(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            val bookEntity = BookEntity(
                authors = book.authors,
                title = book.title,
                year = book.year
            )
            val isFavorite = database.bookDao().isBookFavorite(book.title, book.authors, book.year)
            if (isFavorite) {
                database.bookDao().delete(bookEntity)
            } else {
                database.bookDao().insert(bookEntity)
            }
        }
    }

    fun isFavorite(book: Book): StateFlow<Boolean> {
        val isFav = MutableStateFlow(false)
        viewModelScope.launch {
            isFav.value = database.bookDao().isBookFavorite(book.title, book.authors, book.year)
        }
        return isFav
    }
}

class BookViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}