package org.foi.hr.air.spotly.data

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class User(
    @SerialName("id") val id: Int,
    @SerialName("ime") val ime: String,
    @SerialName("prezime") val prezime: String,
    @SerialName("email") val email: String,
    @SerialName("lozinka") val lozinka: String? = null,
    @SerialName("brojMobitela") val brojMobitela: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("tipKorisnikaId") val tipKorisnikaId: Int?,
    @SerialName("token") val token: String? = null
)
