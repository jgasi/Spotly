package com.example.vehicle_lookup.repository

import com.example.vehicle_lookup.data.Vehicle
import com.example.vehicle_lookup.network.NetworkClient

class VehicleRepository : IVehicleRepository {
    override suspend fun findVehicle(licensePlate: String): Vehicle {
        val response = NetworkClient.vehicleService.lookupVehicle(licensePlate)

        if (response.isSuccessful) {
            val vehicleResponse = response.body()
            if (vehicleResponse != null) {
                return vehicleResponse.data
            } else {
                throw Exception("Vozilo nije pronađeno.")
            }
        } else {
            throw Exception("API error: ${response.code()} ${response.message()}")
        }
    }

    override fun findVehicleByOCR(image: ByteArray): Vehicle {
        TODO("Not yet implemented")
    }
}