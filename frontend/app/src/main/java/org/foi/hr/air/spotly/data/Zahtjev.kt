package org.foi.hr.air.spotly.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Zahtjev(
    @SerialName("id") val id: Int,
    @SerialName("predmet") val predmet: String,
    @SerialName("poruka") val poruka: String,
    @SerialName("odgovor") val odgovor: String?,
    @SerialName("status") val status: String,
    @SerialName("datumVrijeme") val datumVrijeme: String,
    @SerialName("adminId") val adminId: Int?,
    @SerialName("korisnikId") val korisnikId: Int,
    @SerialName("tipZahtjevaId") val tipZahtjevaId: Int?
)
