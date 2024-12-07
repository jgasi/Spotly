package com.example.vehicle_lookup.services

import com.example.vehicle_lookup.data.VehicleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VehicleService {
    @GET("vehicles/lookup")
    suspend fun lookupVehicle(@Query("licensePlate") licensePlate: String): Response<VehicleResponse>
}