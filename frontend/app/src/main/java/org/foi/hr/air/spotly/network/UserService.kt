package org.foi.hr.air.spotly.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import org.foi.hr.air.spotly.data.User
import org.foi.hr.air.spotly.data.UserType
import java.io.IOException
import java.util.logging.Logger

object UserService {
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

    suspend fun fetchUsers(): List<User> {
        val request = Request.Builder()
            .url("$urlBase/Korisnik")
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Greška: $response")

            val json = Json { ignoreUnknownKeys = true }
            val responseBody = response.body!!.string()
            return json.decodeFromString(responseBody)
        }
    }

    suspend fun fetchUserTypes(): Map<Int, String> {
        val request = Request.Builder()
            .url("$urlBase/Korisnik/user-types")
            .build()

        val response = executeRequest(request)
        response.use {
            if (!response.isSuccessful) throw IOException("Greška: $response")

            val json = Json { ignoreUnknownKeys = true }
            val responseBody = response.body!!.string()
            val types = json.decodeFromString<List<UserType>>(responseBody)
            return types.associateBy({ it.id }, { it.tip })
        }
    }

    suspend fun registerUser(user: User): Boolean {
        val jsonBody = Json.encodeToString(user)
        val requestBody = jsonBody.toRequestBody("application/json".toMediaType())

        Log.d("RegisterUser", "URL: $urlBase/Korisnik/register")
        Log.d("RegisterUser", "Request Body: $jsonBody")

        val request = Request.Builder()
            .url("$urlBase/Korisnik/register")
            .post(requestBody)
            .build()

        val response = executeRequest(request)
        response.use {
            Log.d("RegisterUser", "Response code: ${response.code}")
            Log.d("RegisterUser", "Response body: ${response.body?.string()}")
            return response.isSuccessful
        }
    }


    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val requestBody = """
                {
                    "email": "$email",
                    "lozinka": "$password"
                }
            """.trimIndent().toRequestBody("application/json".toMediaTypeOrNull())

            val request = Request.Builder()
                .url("$urlBase/Korisnik/login")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build()

            val response = executeRequest(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Prijava nije uspjela, provjerite podatke i pokušajte ponovno!, ${response.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}