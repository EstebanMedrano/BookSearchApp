package com.example.booksearchapp

data class Book(
    val authors: String?, // Los autores pueden ser nulos según la API
    val title: String?,   // El título puede ser nulo
    val year: String?     // El año puede ser nulo
    // Añade aquí otros campos que la API pueda devolver (por ejemplo, `id`, `thumbnail`, etc.)
)