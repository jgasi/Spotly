package com.example.ws.network.models.responses

import com.example.ws.models.responses.User
import com.example.ws.models.responses.VehicleType
import com.google.gson.annotations.SerializedName

data class Vehicle(
    @SerializedName("id") val id: Int,
    @SerializedName("marka") val marka: String? = null,
    @SerializedName("model") val model: String? = null,
    @SerializedName("godiste") val godiste: String? = null,
    @SerializedName("registracija") val registracija: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("tipVozilaId") val tipVozilaId: Int? = null,
    @SerializedName("korisnikId") val korisnikId: Int? = null,
    @SerializedName("korisnik") val korisnik: User? = null,
    @SerializedName("tipVozila") val tipVozila: VehicleType? = null
)