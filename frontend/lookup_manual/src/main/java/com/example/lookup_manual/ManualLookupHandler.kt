package com.example.lookup_manual

import android.content.Context
import com.example.core.vehicle_lookup.*
import com.example.core.vehicle_lookup.network.*
import com.example.core.vehicle_lookup.network.models.*
import com.example.ws.models.responses.User
import com.example.ws.models.responses.VehicleType
import com.example.ws.network.models.responses.Vehicle
import com.example.ws.request_handlers.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ManualLookupHandler(
    private val context: Context,
    private val offlineDataSource: VehicleLookupDataSource
) : LookupHandler {
    private val networkManager = NetworkManager(context)

    override fun handleLookup(licensePlate: String, token: String, lookupListner: LookupOutcomeListener) {
        require(licensePlate is String) { "Must receive String instance for 'licensePlate'!" }

        if (!networkManager.isNetworkAvailable()) {
            tryOfflineLookup(licensePlate, lookupListner)
        } else {

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
                            korisnik = it.korisnik?.toUserData(),
                            tipVozila = it.tipVozila?.toVehicleTypeData()
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

                fun VehicleType.toVehicleTypeData(): VehicleTypeData {
                    return VehicleTypeData(
                        id = this.id,
                        tip = this.tip
                    )
                }
            }, token)
        }
    }

    private fun tryOfflineLookup(licensePlate: String, lookupListner: LookupOutcomeListener) {
        CoroutineScope(Dispatchers.IO).launch {
            val vehicle = offlineDataSource.getVehicleByLicensePlate(licensePlate)
            if (vehicle != null) {
                withContext(Dispatchers.Main) {
                    lookupListner.onSuccessfulLookup(vehicle)
                }
            } else {
                withContext(Dispatchers.Main) {
                    lookupListner.onFailedLookup("Vozilo nije pronađeno", 404)
                }
            }
        }
    }
}