package com.example.core.vehicle_lookup

interface LookupHandler {
    fun handleLookup(licensePlate: String, token: String, lookupListner: LookupOutcomeListener)
}