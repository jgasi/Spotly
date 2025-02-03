package com.example.ws.request_handlers

import com.example.core.vehicle_lookup.network.*
import com.example.core.vehicle_lookup.network.models.*
import com.google.gson.Gson
import retrofit2.*


abstract class TemplateRequestHandler<T> : RequestHandler<T> {
    override fun sendRequest(responseListener: ResponseListener<T>, token: String) {
        val serviceCall = getServiceCall(token)

        serviceCall.enqueue(object : Callback<SuccessfulResponseBody<T>> {
            override fun onResponse(
                call: Call<SuccessfulResponseBody<T>>,
                response: Response<SuccessfulResponseBody<T>>
            ) {
                if (response.isSuccessful) {
                    responseListener.onSuccessfulResponse(response.body() as SuccessfulResponseBody<T>)
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.string(),
                        ErrorResponseBody::class.java
                    )
                    responseListener.onErrorResponse(errorResponse)
                }
            }

            override fun onFailure(call: Call<SuccessfulResponseBody<T>>, t: Throwable) {
                responseListener.onNetworkFailiure(t)
            }
        })
    }

    protected abstract fun getServiceCall(token: String): Call<SuccessfulResponseBody<T>>
}