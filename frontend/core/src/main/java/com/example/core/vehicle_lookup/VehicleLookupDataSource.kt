package com.example.core.vehicle_lookup

interface VehicleLookupDataSource {
    suspend fun getVehicleByLicensePlate(licensePlate: String): VehicleData?
}