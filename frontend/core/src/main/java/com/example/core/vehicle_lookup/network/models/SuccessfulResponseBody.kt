package com.example.core.vehicle_lookup.network.models

class SuccessfulResponseBody<T>(
    success: Boolean, message: String, val data: Array<T>
) : ResponseBody(success, message)