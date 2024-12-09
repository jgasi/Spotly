package com.example.core.vehicle_lookup

interface LookupOutcomeListener {
    fun onSuccessfulLookup(vehicleData: VehicleData)
    fun onFailedLookup(reason: String, status: Int? = null)
}