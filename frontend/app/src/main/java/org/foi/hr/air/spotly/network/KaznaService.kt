package org.foi.hr.air.spotly.network

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.foi.hr.air.spotly.data.Kazna
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import java.io.IOException

object KaznaService {
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

    suspend fun fetchKazneForUser(korisnikId: Int): List<Kazna> {
        val request = Request.Builder()
            .url("$urlBase/kazna/user/$korisnikId")
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Greška: $response")
            val json = Json { ignoreUnknownKeys = true }
            val responseBody = response.body!!.string()
            return json.decodeFromString(responseBody)
        }
    }

    suspend fun fetchKazneForUserr(korisnikId: Int): List<Kazna>? {
        val url = "$urlBase/Kazna/user/$korisnikId"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        var response: Response? = null

        return try {
            response = executeRequest(request)

            if (response?.isSuccessful == true) {
                response.body?.string()?.let { responseBody ->
                    val json = Json { ignoreUnknownKeys = true }
                    json.decodeFromString<List<Kazna>>(responseBody)
                }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            response?.body?.close()
        }
    }

    suspend fun addKazna(kazna: Kazna): Boolean {
        val json = Json.encodeToString(kazna)
        val requestBody = Json.encodeToString(kazna).toRequestBody(jsonMediaType)

        val request = Request.Builder()
            .url("$urlBase/kazna")
            .post(requestBody)
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Greška: $response")
            return response.isSuccessful
        }
    }

    suspend fun deleteKazna(kaznaId: Int): Boolean {
        val request = Request.Builder()
            .url("$urlBase/kazna/$kaznaId")
            .delete()
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Greška: $response")
            return response.isSuccessful
        }
    }

}