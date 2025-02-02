package org.foi.hr.air.spotly.data

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class TipMjesta(
    @SerialName("id") val id: Int,
    @SerialName("tip") val tip: String
)
