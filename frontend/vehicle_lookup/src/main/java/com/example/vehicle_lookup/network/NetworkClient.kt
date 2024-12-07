package com.example.vehicle_lookup.network

import com.example.vehicle_lookup.services.VehicleService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5010/api/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val vehicleService: VehicleService = retrofit.create(VehicleService::class.java)
}