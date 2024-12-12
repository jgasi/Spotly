package com.example.ws.models.responses

data class User(
    val id: Int,
    val ime: String?,
    val prezime: String?,
    val email: String?,
    val brojMobitela: String?,
    val status: String?
)
