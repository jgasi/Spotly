package org.foi.hr.air.spotly.data

@kotlinx.serialization.Serializable
data class User(
    val id: Int,
    val ime: String,
    val prezime: String,
    val email: String,
    val brojMobitela: String,
    val status: String,
    val tipKorisnikaId: Int
)
