package org.foi.hr.air.spotly.data

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ParkingSpace (
    @SerialName("id") val id: Int,
    @SerialName("status") val status: String,
    @SerialName("dostupnost") val dostupnost: String,
    @SerialName("tipMjestaId") val tipMjestaId: Int = 0,
    @SerialName("rezervacijas") val rezervacijas: List<String> = emptyList(),
    @SerialName("tipMjesta") val tipMjesta: Int? = null
)