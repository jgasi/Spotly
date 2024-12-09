package com.example.core.vehicle_lookup

data class VehicleData(
    val id: Int,
    val marka: String?,
    val model: String?,
    val godiste: String?,
    val registracija: String?,
    val status: String?,
    val tipVozilaId: Int?,
    val korisnikId: Int?,
    val korisnik: UserData?,
    val tipVozila: VehicleTypeData?
)
