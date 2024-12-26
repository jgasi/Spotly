package org.foi.hr.air.spotly.network

import android.util.Log
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    suspend inline fun <reified T> getAll(): List<T> = withContext(Dispatchers.IO) {
        val rawClassName = T::class.simpleName!!
        var className = rawClassName.filter { it.isLetter() }.lowercase()

        if (className == "tipkorisnika") {
            className = "korisnik/user-types"
        }

        val request = Request.Builder()
            .url("$urlBase/$className")
            .build()

        val response = executeRequest(request)
        response.use {
            Log.d("ApiService", "response: ${response}")
            when (response.code) {
                404 -> return@withContext emptyList<T>()
                200 -> return@withContext json.decodeFromString(response.body!!.string())
                else -> throw IOException("Error fetching data: ${response.code} - ${response.message}")
            }
        }
    }
}