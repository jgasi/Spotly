package org.foi.hr.air.spotly.network

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.foi.hr.air.spotly.data.UserStore
import org.foi.hr.air.spotly.data.Vehicle
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object VoziloService {
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

    suspend fun fetchVehicleByLicensePlate(licensePlate: String): Vehicle {
        val request = Request.Builder()
            .url("$urlBase/Vozilo/$licensePlate")
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Error: $response")

            val json = Json { ignoreUnknownKeys = true }
            val responseBody = response.body!!.string()
            return json.decodeFromString(responseBody)
        }
    }

    suspend fun fetchVehicleByUserId(id: Int): Vehicle {
        val request = Request.Builder()
            .url("$urlBase/Vozilo/korisnik/$id")
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Error: $response")
            val json = Json { ignoreUnknownKeys = true }
            val responseBody = response.body!!.string()
            val types = json.decodeFromString<Vehicle>(responseBody)
            return types
        }
    }

    suspend fun registerVehicle(vehicleData: Vehicle): Boolean {
        val jsonBody = Json.encodeToString(vehicleData)
        val requestBody = jsonBody.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$urlBase/Vozilo/register")
            .post(requestBody)
            .build()

        val response = executeRequest(request)
        response.use {
            return response.isSuccessful
        }
    }
}