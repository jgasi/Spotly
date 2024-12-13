package org.foi.hr.air.spotly.data

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Vehicle(
    @SerialName("id") val id: Int,
    @SerialName("marka") val marka: String?,
    @SerialName("model") val model: String?,
    @SerialName("godiste") val godiste: String?,
    @SerialName("registracija") val registracija: String?,
    @SerialName("status") val status: String?,
    @SerialName("tipVozilaId") val tipVozilaId: Int?,
    @SerialName("korisnikId") val korisnikId: Int?
)
