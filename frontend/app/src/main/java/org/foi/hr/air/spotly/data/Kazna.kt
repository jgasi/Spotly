package org.foi.hr.air.spotly.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Kazna(
    @SerialName("id") val id: Int,
    @SerialName("razlog") val razlog: String,
    @SerialName("novcaniIznos") val novcaniIznos: String,
    @SerialName("datumVrijeme") val datumVrijeme: String,
    @SerialName("adminId") val adminId: Int,
    @SerialName("korisnikId") val korisnikId: Int,
    @SerialName("tipKazneId") val tipKazneId: Int
)
