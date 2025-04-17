package com.example.booksearchapp.network.model

data class VolumeInfo(
    val authors: List<String>?,
    val title: String?,
    val publishedDate: String? // La API de Google Books devuelve la fecha completa
    // Añade aquí otros campos del libro que necesites de la API
)