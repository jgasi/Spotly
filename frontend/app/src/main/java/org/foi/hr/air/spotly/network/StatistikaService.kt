package org.foi.hr.air.spotly.network

import ParkingStatistics
import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object StatistikaService {
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

    // Funkcija za dohvaćanje ukupnog broja kazni
    suspend fun fetchTotalKazneCount(): Int {
        val request = Request.Builder()
            .url("$urlBase/Kazna/statistics/total")
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Greška pri dohvaćanju ukupnog broja kazni: $response")
            return response.body!!.string().toInt()
        }
    }

    // Funkcija za dohvaćanje broja kazni po korisniku
    suspend fun fetchKazneCountByUserId(userId: Int): Int {
        val request = Request.Builder()
            .url("$urlBase/Kazna/statistics/user/$userId")
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Greška pri dohvaćanju broja kazni za korisnika: $response")
            return response.body!!.string().toInt()
        }
    }

    // Nova funkcija za dohvaćanje statistike parkirnih mjesta
    suspend fun fetchParkingStatistics(): ParkingStatistics {
        val request = Request.Builder()
            .url("$urlBase/ParkingMjesto/statistics")
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Greška pri dohvaćanju statistike parkirnih mjesta: $response")

            val json = Json { ignoreUnknownKeys = true }
            val responseBody = response.body!!.string()
            Log.e("StatistikaService","Odgovor servera: $responseBody")
            return json.decodeFromString(responseBody)
        }
    }
}