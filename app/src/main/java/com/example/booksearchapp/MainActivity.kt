package com.example.booksearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.booksearchapp.data.local.AppDatabase
import com.example.booksearchapp.ui.BookViewModel
import com.example.booksearchapp.data.local.BookEntity
import com.example.booksearchapp.ui.theme.BookSearchAppTheme
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookSearchAppTheme {
                val context = LocalContext.current
                val database = AppDatabase.getDatabase(context)
                val viewModel: BookViewModel = viewModel(factory = BookViewModelFactory(database))
                BookSearchApp(viewModel = viewModel)
            }
        }
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

@Composable
fun BookSearchApp(viewModel: BookViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()
    val favoriteBooks by viewModel.favoriteBooks.collectAsState(initial = emptyList())
    var showFavoritesScreen by remember { mutableStateOf(false) }

    if (showFavoritesScreen) {
        FavoritesScreen(favoriteBooks = favoriteBooks, onBack = { showFavoritesScreen = false }, onToggleFavorite = viewModel::toggleFavorite)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Título del Libro") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { viewModel.searchBooks(searchQuery) }) {
                Text("Buscar Libros")
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (searchResults.isNotEmpty()) {
                Text("Resultados de la Búsqueda:", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(8.dp))
                SearchResultsList(books = searchResults, onToggleFavorite = viewModel::toggleFavorite, isFavorite = viewModel::isFavorite)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = { showFavoritesScreen = true }) {
                Text("Ver Libros Guardados")
            }
        }
    }
}

@Composable
fun SearchResultsList(books: List<Book>, onToggleFavorite: (Book) -> Unit, isFavorite: (Book) -> StateFlow<Boolean>) {
    LazyColumn {
        items(books) { book ->
            BookItem(book = book, onToggleFavorite = onToggleFavorite, isFavorite = isFavorite(book).collectAsState().value)
            Divider()
        }
    }
}

@Composable
fun BookItem(book: Book, onToggleFavorite: (Book) -> Unit, isFavorite: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = book.title.orEmpty(), style = MaterialTheme.typography.subtitle1)
            Text(text = book.authors.orEmpty(), style = MaterialTheme.typography.body2)
            Text(text = book.year.orEmpty(), style = MaterialTheme.typography.caption)
        }
        IconButton(onClick = { onToggleFavorite(book) }) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos"
            )
        }
    }
}

@Composable
fun FavoritesScreen(favoriteBooks: List<BookEntity>, onBack: () -> Unit, onToggleFavorite: (Book) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onBack) {
            Text("Volver")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Libros Guardados", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        if (favoriteBooks.isEmpty()) {
            Text("No tienes libros guardados.")
        } else {
            LazyColumn {
                items(favoriteBooks) { bookEntity ->
                    val book = Book(bookEntity.authors, bookEntity.title, bookEntity.year)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = book.title.orEmpty(), style = MaterialTheme.typography.subtitle1)
                            Text(text = book.authors.orEmpty(), style = MaterialTheme.typography.body2)
                            Text(text = book.year.orEmpty(), style = MaterialTheme.typography.caption)
                        }
                        IconButton(onClick = { onToggleFavorite(book) }) {
                            Icon(Icons.Filled.Favorite, contentDescription = "Quitar de favoritos")
                        }
                    }
                    Divider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BookSearchAppTheme {
        val context = LocalContext.current
        val database = AppDatabase.getDatabase(context)
        val viewModel: BookViewModel = viewModel(factory = BookViewModelFactory(database))
        BookSearchApp(viewModel = viewModel)
    }
}