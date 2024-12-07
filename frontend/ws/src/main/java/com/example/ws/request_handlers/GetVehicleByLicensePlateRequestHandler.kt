package com.example.ws.request_handlers

import android.util.Log
import com.example.core.vehicle_lookup.network.ResponseListener
import com.example.core.vehicle_lookup.network.models.*
import com.example.ws.network.NetworkService
import com.example.ws.network.models.responses.Vehicle
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetVehicleByLicensePlateRequestHandler(private val licensePlate: String): TemplateRequestHandler<Vehicle>() {
    override fun getServiceCall(): Call<SuccessfulResponseBody<Vehicle>> {
        val service = NetworkService.vozilaService
        Log.d("GetVehicleByLicensePlateRequestHandler", "Preparing service call for $licensePlate")
        return service.lookupVehicle(licensePlate)
    }

    override fun sendRequest(responseListener: ResponseListener<Vehicle>) {
        val serviceCall = getServiceCall()

        serviceCall.enqueue(object : Callback<SuccessfulResponseBody<Vehicle>> {
            override fun onResponse(
                call: Call<SuccessfulResponseBody<Vehicle>>,
                response: Response<SuccessfulResponseBody<Vehicle>>
            ) {
                if (response.isSuccessful) {
                    Log.d("Response", "Body: ${response.body()}")

                    val body = response.body()
                    if (body != null) {
                        val vehicleArray = body.data
                        if (!vehicleArray.isNullOrEmpty()) {
                            responseListener.onSuccessfulResponse(body)
                        } else {
                            try {
                                val singleVehicle = Gson().fromJson(response.body().toString(), Vehicle::class.java)
                                if (singleVehicle != null) {
                                    responseListener.onSuccessfulResponse(
                                        SuccessfulResponseBody(
                                            success = true,
                                            message = "Pronađeno vozilo",
                                            data = arrayOf(singleVehicle)
                                        )
                                    )
                                } else {
                                    responseListener.onErrorResponse(
                                        ErrorResponseBody(false, "Nije pronađeno nijedno vozilo za zadanu tablicu", 404, "Not Found")
                                    )
                                }
                            } catch (e: Exception) {
                                Log.e("Response", "Parsing single vehicle failed", e)
                                responseListener.onErrorResponse(
                                    ErrorResponseBody(false, "Greška prilikom parsiranja odgovora", 500, "Internal Server Error")
                                )
                            }
                        }
                    } else {
                        responseListener.onErrorResponse(
                            ErrorResponseBody(false, "Prazan odgovor servera", 500, "Internal Server Error")
                        )
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = try {
                        Gson().fromJson(errorBody, ErrorResponseBody::class.java)
                            ?: ErrorResponseBody(false, response.message(), response.code(), "Error")
                    } catch (e: Exception) {
                        ErrorResponseBody(false, errorBody ?: "Parsing error", response.code(), "Error")
                    }
                    responseListener.onErrorResponse(errorResponse)
                }
            }

            override fun onFailure(call: Call<SuccessfulResponseBody<Vehicle>>, t: Throwable) {
                responseListener.onNetworkFailiure(t)
            }
        })
    }
}