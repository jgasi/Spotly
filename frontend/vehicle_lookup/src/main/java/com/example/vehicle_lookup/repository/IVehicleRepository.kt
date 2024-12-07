package com.example.vehicle_lookup.repository

import com.example.vehicle_lookup.data.Vehicle

interface IVehicleRepository {
    suspend fun findVehicle(licensePlate: String): Vehicle
    fun findVehicleByOCR(image: ByteArray): Vehicle
}