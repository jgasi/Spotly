package org.foi.hr.air.spotly.network

import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ApiService() {
    val urlBase: String = "http://10.0.2.2:5010/api"
    private val client = OkHttpClient()
    val json = Json { ignoreUnknownKeys = true }

    suspend fun executeRequest(request: Request): Response = suspendCoroutine { continuation ->
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                continuation.resume(response)
            }
        })
    }

    suspend inline fun <reified T> getAll(): List<T> {
        val className = T::class.simpleName!!.lowercase()
        val request = Request.Builder()
            .url("$urlBase/$className")
            .build()

        val response = this.executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Error: $response")
            return json.decodeFromString(response.body!!.string())
        }
    }
}