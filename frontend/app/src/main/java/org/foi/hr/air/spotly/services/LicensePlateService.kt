package org.foi.hr.air.spotly.services

import org.foi.hr.air.spotly.data.Vehicle

interface LicensePlateService {
    suspend fun getVehicleData(licensePlate: String): Vehicle
}