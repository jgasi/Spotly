package org.foi.hr.air.spotly.network

import android.util.Log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.foi.hr.air.spotly.data.ParkingSpace
import org.foi.hr.air.spotly.data.UserStore
import org.foi.hr.air.spotly.data.Reservation
import org.foi.hr.air.spotly.data.ReservationPS
import org.foi.hr.air.spotly.data.UserStore
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object ParkingMjestoService {
    private val urlBase = "http://10.0.2.2:5010/api"
    private val client = OkHttpClient()


    private suspend fun executeRequest(request: Request): Response = suspendCoroutine { continuation ->
        val userToken = UserStore.getUser()?.token

        val finalRequest = userToken?.let {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $it")
                .build()
        } ?: request

        client.newCall(finalRequest).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                continuation.resume(response)
            }
        })
    }

    suspend fun fetchParkingSpaces(): List<ParkingSpace> {
        val url = "${urlBase}/ParkingMjesto"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Greška: $response")

            val json = Json { ignoreUnknownKeys = true }
            val responseBody = response.body!!.string()
            val parkingSpace = json.decodeFromString<List<ParkingSpace>>(responseBody)
            return parkingSpace
        }
    }


    suspend fun updateParkingSpace(parkingSpace: ParkingSpace): Boolean {
        val requestBody = Json.encodeToString(parkingSpace).toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("$urlBase/ParkingMjesto/")
            .put(requestBody)
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) {
                Log.e("ParkingSpace", "Greška prilikom ažuriranja: $response")
                return false
            }
            return true
        }
    }

    suspend fun fetchParkingSpaceById(id: Int): ParkingSpace {
        val request = Request.Builder()
            .url("$urlBase/ParkingMjesto/$id")
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Greška: $response")

            val json = Json { ignoreUnknownKeys = true }
            val responseBody = response.body!!.string()
            Log.d("ParkingSpace", "Parking space is: $responseBody")
            return json.decodeFromString<ParkingSpace>(responseBody)
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
            parkingMjestoId = parkingSpaceId,
            voziloId = voziloId,
            datumVrijemeRezervacije = reservationStartTime,
            datumVrijemeOdlaska = reservationEndTime
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

    suspend fun blockParkingSpace(id: Int): Boolean {
        val request = Request.Builder()
            .url("$urlBase/ParkingMjesto/blokiraj/$id")
            .put("".toRequestBody(null))
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) {
                Log.e("ParkingSpace", "Greška prilikom blokiranja: $response")
                return false
            }
            return true
        }
    }

    suspend fun unblockParkingSpace(id: Int): Boolean {
        val request = Request.Builder()
            .url("$urlBase/ParkingMjesto/odblokiraj/$id")
            .put("".toRequestBody(null))
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) {
                Log.e("ParkingSpace", "Greška prilikom deblokiranja: $response")
                return false
            }
            return true
        }
    }

}