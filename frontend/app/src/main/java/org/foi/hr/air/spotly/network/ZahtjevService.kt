package org.foi.hr.air.spotly.network

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.foi.hr.air.spotly.data.Zahtjev
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object ZahtjevService {
    private val urlBase = "http://10.0.2.2:5010/api"
    private val client = OkHttpClient()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

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

    suspend fun addZahtjev(zahtjev: Zahtjev): Boolean {
        val url = "$urlBase/Zahtjev"
        val requestBody = Json.encodeToString(zahtjev).toRequestBody(jsonMediaType)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        return try {
            val response = executeRequest(request)
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}