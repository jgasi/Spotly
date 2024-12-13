package com.example.core.vehicle_lookup.network

import com.example.core.vehicle_lookup.network.models.*

interface ResponseListener<T> {
    fun onSuccessfulResponse(response: SuccessfulResponseBody<T>)
    fun onErrorResponse(response: ErrorResponseBody)
    fun onNetworkFailiure(t: Throwable)
}