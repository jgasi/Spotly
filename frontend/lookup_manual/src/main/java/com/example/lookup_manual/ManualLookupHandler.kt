package com.example.lookup_manual

import com.example.core.vehicle_lookup.*
import com.example.core.vehicle_lookup.network.*
import com.example.core.vehicle_lookup.network.models.*
import com.example.ws.models.responses.User
import com.example.ws.network.models.responses.Vehicle
import com.example.ws.request_handlers.*

class ManualLookupHandler : LookupHandler {
    override fun handleLookup(licensePlate: String, lookupListner: LookupOutcomeListener) {
        require(licensePlate is String) { "Must receive String instance for 'licensePlate'!" }

        val requestHandler = GetVehicleByLicensePlateRequestHandler(licensePlate)
        requestHandler.sendRequest(object : ResponseListener<Vehicle> {
            override fun onSuccessfulResponse(response: SuccessfulResponseBody<Vehicle>) {
                val vehicle = response.data.firstOrNull()
                vehicle?.let {
                    val vehicleData = VehicleData(
                        id = it.id,
                        marka = it.marka,
                        model = it.model,
                        godiste = it.godiste,
                        registracija = it.registracija,
                        status = it.status,
                        tipVozilaId = it.tipVozilaId,
                        korisnikId = it.korisnikId,
                        korisnik = it.korisnik?.toUserData()
                    )
                    lookupListner.onSuccessfulLookup(vehicleData)
                }
            }

            override fun onErrorResponse(error: ErrorResponseBody) {
                lookupListner.onFailedLookup(error.message, error.status)
            }

            override fun onNetworkFailiure(t: Throwable) {
                lookupListner.onFailedLookup("Network error: ${t.message}")
            }

            fun User.toUserData(): UserData {
                return UserData(
                    id = this.id,
                    ime = this.ime,
                    prezime = this.prezime,
                    email = this.email,
                    brojMobitela = this.brojMobitela,
                    status = this.status
                )
            }
        })
    }
}