package com.example.core.vehicle_lookup.network

interface RequestHandler<T> {
    fun sendRequest(responseListener: ResponseListener<T>)
}