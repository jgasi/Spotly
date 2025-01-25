package org.foi.hr.air.spotly.network

import android.util.Log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.foi.hr.air.spotly.data.ParkingSpace
import org.foi.hr.air.spotly.data.Reservation
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object ParkingMjestoService {
    private val urlBase = "http://10.0.2.2:5010/api"
    private val client = OkHttpClient()


    private suspend fun executeRequest(request: Request): Response = suspendCoroutine { continuation ->
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                continuation.resume(response)
            }
        })
    }

    suspend fun fetchParkingSpaces(): List<ParkingSpace> {
        val request = Request.Builder()
            .url("$urlBase/ParkingMjesto")
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Greška: $response")

            val json = Json { ignoreUnknownKeys = true }
            val responseBody = response.body!!.string()
            Log.d("ParkingSpace", "Parking space is: ${responseBody}")
            return json.decodeFromString(responseBody)
        }
    }

    suspend fun reserveParkingSpace(
        parkingSpaceId: Int,
        voziloId: Int,
        reservationStartTime: String,
        reservationEndTime: String
    ): Boolean {
        val reservationDTO = Reservation(
            id = 2,
            parkingSpaceId = parkingSpaceId,
            vehicleId = voziloId,
            reservationStartTime = reservationStartTime,
            reservationEndTime = reservationEndTime
        )

        val json = Json { ignoreUnknownKeys = true }
        val requestBody = json.encodeToString(reservationDTO).toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$urlBase/ParkingMjesto")
            .post(requestBody)
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Reservation failed: $response")

            Log.d("ParkingSpace", "Reservation successful: ${response.body!!.string()}")
            return true
        }
    }
}