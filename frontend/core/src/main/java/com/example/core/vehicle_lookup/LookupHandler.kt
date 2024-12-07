package com.example.core.vehicle_lookup

interface LookupHandler {
    fun handleLookup(licensePlate: String, lookupListner: LookupOutcomeListener)
}