package com.example.ws.network

import com.example.core.vehicle_lookup.network.models.SuccessfulResponseBody
import com.example.ws.network.models.responses.Vehicle
import retrofit2.*
import retrofit2.http.*

interface VehicleService {
    @GET("Vozilo/vehicles/lookup")
    fun lookupVehicle(@Query("licensePlate") licensePlate: String): Call<SuccessfulResponseBody<Vehicle>>
}