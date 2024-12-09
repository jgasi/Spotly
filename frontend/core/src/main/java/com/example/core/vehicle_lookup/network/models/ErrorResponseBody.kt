package com.example.core.vehicle_lookup.network.models

class ErrorResponseBody(
    success: Boolean,
    message: String,
    status: Int,
) : ResponseBody(success, message, status)