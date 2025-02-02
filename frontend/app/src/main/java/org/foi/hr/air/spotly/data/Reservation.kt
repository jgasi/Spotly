package org.foi.hr.air.spotly.data

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Reservation(
    @SerialName("id") val id: Int,
    @SerialName("parkingMjestoId") val parkingSpaceId: Int,
    @SerialName("voziloId") val vehicleId: Int,
    @SerialName("datumVrijemeRezervacije") val reservationStartTime: String,
    @SerialName("datumVrijemeOdlaska") val reservationEndTime: String
)