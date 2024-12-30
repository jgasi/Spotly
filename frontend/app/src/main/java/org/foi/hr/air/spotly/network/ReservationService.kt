package org.foi.hr.air.spotly.network

import android.util.Log
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.MediaType.Companion.toMediaType
import org.foi.hr.air.spotly.data.Reservation
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object ReservationService {
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

    suspend fun fetchReservations(): List<Reservation> {
        val request = Request.Builder()
            .url("$urlBase/Rezervacija")
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Error: $response")

            val json = Json { ignoreUnknownKeys = true }
            val responseBody = response.body!!.string()
            Log.d("ReservationService", "Reservations: $responseBody")
            return json.decodeFromString(responseBody)
        }
    }

    suspend fun fetchReservationById(id: Int): Reservation? {
        val request = Request.Builder()
            .url("$urlBase/Rezervacija/$id")
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Error: $response")

            val json = Json { ignoreUnknownKeys = true }
            val responseBody = response.body!!.string()
            Log.d("ReservationService", "Reservation: $responseBody")
            return json.decodeFromString(responseBody)
        }
    }

    suspend fun fetchReservationByVehicleId(vehicleId: Int): Reservation? {
        val request = Request.Builder()
            .url("$urlBase/Rezervacija/vozilo/$vehicleId")
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Error: $response")

            val json = Json { ignoreUnknownKeys = true }
            val responseBody = response.body!!.string()
            Log.d("ReservationService", "Reservation by vehicle: $responseBody")
            return json.decodeFromString(responseBody)
        }
    }


    suspend fun createReservation(reservation: Reservation): Boolean {
        val json = Json.encodeToString(reservation)
        val requestBody = RequestBody.create("application/json".toMediaType(), json)  // Corrected here

        val request = Request.Builder()
            .url("$urlBase/Rezervacija")
            .post(requestBody)
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Error creating reservation: $response")
            Log.d("ReservationService", "Reservation successfully created.")
            return true
        }
    }

    suspend fun updateReservation(reservation: Reservation): Boolean {
        val json = Json.encodeToString(reservation)
        val requestBody = RequestBody.create("application/json".toMediaType(), json)  // Corrected here

        val request = Request.Builder()
            .url("$urlBase/Rezervacija")
            .put(requestBody)
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Error updating reservation: $response")
            Log.d("ReservationService", "Reservation successfully updated.")
            return true
        }
    }

    suspend fun deleteReservation(id: Int): Boolean {
        val request = Request.Builder()
            .url("$urlBase/Rezervacija/$id")
            .delete()
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Error deleting reservation: $response")
            Log.d("ReservationService", "Reservation successfully deleted.")
            return true
        }
    }
}
