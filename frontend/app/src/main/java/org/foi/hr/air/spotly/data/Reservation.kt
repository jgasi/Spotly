package org.foi.hr.air.spotly.data

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Reservation(
    @SerialName("id") val id: Int,
    @SerialName("datum_vrijeme_rezervacije") val datumVrijemeRezervacije: String,
    @SerialName("datum_vrijeme_odlaska") val datumVrijemeOdlaska: String?,
    @SerialName("voziloId") val voziloId: Int?,
    @SerialName("parking_mjestoId") val parkingMjestoId: Int?
)
