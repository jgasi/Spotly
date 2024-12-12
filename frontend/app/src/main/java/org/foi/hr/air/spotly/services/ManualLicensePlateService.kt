package org.foi.hr.air.spotly.services

import org.foi.hr.air.spotly.data.Vehicle
import org.foi.hr.air.spotly.network.VoziloService

class ManualLicensePlateService : LicensePlateService {
    override suspend fun getVehicleData(licensePlate: String): Vehicle {
        val vehicleData = VoziloService.fetchVehicleByLicensePlate(licensePlate)
        return vehicleData
    }
}