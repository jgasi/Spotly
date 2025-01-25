package org.foi.hr.air.spotly.data

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Reservation(
    @SerialName("Id") val id: Int,
    @SerialName("Parking_mjestoId") val parkingSpaceId: Int,
    @SerialName("VoziloId") val vehicleId: Int,
    @SerialName("Datum_vrijeme_rezervacije") val reservationStartTime: String,
    @SerialName("Datum_vrijeme_odlaska") val reservationEndTime: String
)