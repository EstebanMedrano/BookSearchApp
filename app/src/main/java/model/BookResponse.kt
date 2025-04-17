package com.example.booksearchapp.network.model

data class BookResponse(
    val items: List<VolumeInfoWrapper>?
    // Otros campos de la respuesta de la API si existen
)