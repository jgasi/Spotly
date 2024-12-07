package com.example.lookup_manual

import com.example.core.vehicle_lookup.LookupHandler
import com.example.core.vehicle_lookup.LookupOutcomeListener
import com.example.core.vehicle_lookup.VehicleData

class ManualLookupHandler : LookupHandler {
    override fun handleLookup(licensePlate: String, lookupListner: LookupOutcomeListener) {
        if (licensePlate !is String) {
            throw IllegalArgumentException("Must receive String instance for 'licensePlate'!")
        }

        if (licensePlate == "ZG1234AB") {
            val sampleData = VehicleData(
                id = 1,
                marka = "Toyota",
                model = "Corolla",
                godiste = "2020",
                registracija = "ZG1234AB",
                status = "Active",
                tipVozilaId = 1,
                korisnikId = 1
            )
            lookupListner.onSuccessfulLookup(sampleData)
        } else {
            lookupListner.onFailedLookup(
                "Wrong mock credentials entered. The correct licensePlate is 'ZG1234AB'!"
            )
        }
    }
}