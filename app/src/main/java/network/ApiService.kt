package com.example.booksearchapp.network

import com.example.booksearchapp.network.model.BookResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("volumes") // Endpoint para buscar libros en la API de Google Books
    suspend fun searchBooks(
        @Query("q") query: String // Parámetro para el título del libro
        // Puedes añadir otros parámetros de consulta si la API los requiere (por ejemplo, "maxResults")
    ): BookResponse
}