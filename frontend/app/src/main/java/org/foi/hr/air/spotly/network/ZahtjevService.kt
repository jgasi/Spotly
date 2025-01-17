package org.foi.hr.air.spotly.network

import android.util.Log
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

    suspend fun getPagedZahtjevi(pageNumber: Int, pageSize: Int): List<Zahtjev>? {
        val url = "$urlBase/Zahtjev/paginated?pageNumber=$pageNumber&pageSize=$pageSize"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        var response: Response? = null

        return try {
            response = executeRequest(request)

            if (response?.isSuccessful == true) {
                response.body?.string()?.let { responseBody ->
                    Json.decodeFromString<List<org.foi.hr.air.spotly.data.Zahtjev>>(responseBody)
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

    suspend fun getZahtjeviNaCekanju(): List<Zahtjev>? {
        val url = "$urlBase/Zahtjev/nacekanju"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        var response: Response? = null

        return try {
            response = executeRequest(request)

            if (response?.isSuccessful == true) {
                response.body?.string()?.let { responseBody ->
                    Json.decodeFromString<List<Zahtjev>>(responseBody)
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

    suspend fun getPagedZahtjeviNaCekanju(pageNumber: Int, pageSize: Int): List<Zahtjev>? {
        val url = "$urlBase/Zahtjev/paginated_na_cekanju?pageNumber=$pageNumber&pageSize=$pageSize"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        var response: Response? = null

        return try {
            response = executeRequest(request)

            if (response?.isSuccessful == true) {
                response.body?.string()?.let { responseBody ->
                    Json.decodeFromString<List<Zahtjev>>(responseBody)
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

    suspend fun getPagedZahtjeviOdgovoreni(pageNumber: Int, pageSize: Int): List<Zahtjev>? {
        val url = "$urlBase/Zahtjev/paginated_odgovoreni?pageNumber=$pageNumber&pageSize=$pageSize"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        var response: Response? = null

        return try {
            response = executeRequest(request)

            if (response?.isSuccessful == true) {
                response.body?.string()?.let { responseBody ->
                    Json.decodeFromString<List<Zahtjev>>(responseBody)
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


    suspend fun getAllZahtjevi(): List<Zahtjev>? {
        val url = "$urlBase/Zahtjev"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        return try {
            val response = executeRequest(request)
            if (response.isSuccessful) {
                response.body?.string()?.let { responseBody ->
                    Json.decodeFromString<List<Zahtjev>>(responseBody)
                }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    suspend fun addZahtjev(zahtjev: Zahtjev): Boolean {
        //if (!QueueService.hasInternet()) {
        //    QueueService.addToQueue(zahtjev)
        //    Log.d("ZahtjevService", "Internet nije dostupan. Zahtjev dodan u red čekanja.")
         //   return false
        //}
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
            QueueService.addToQueue(zahtjev)
            Log.e("ZahtjevService", "Greška prilikom slanja zahtjeva. Zahtjev stavljen u red čekanja. ${e.message}")
            false
        }
    }

    suspend fun updateZahtjev(zahtjev: Zahtjev): Boolean {
        val url = "$urlBase/Zahtjev"
        val requestBody = Json.encodeToString(zahtjev).toRequestBody(jsonMediaType)

        val request = Request.Builder()
            .url(url)
            .put(requestBody)
            .build()

        return try {
            val response = executeRequest(request)
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    suspend fun getZahtjevById(id: Int): Zahtjev? {
        val url = "$urlBase/Zahtjev/$id"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        return try {
            val response = executeRequest(request)
            if (response.isSuccessful) {
                response.body?.string()?.let { responseBody ->
                    Json.decodeFromString<Zahtjev>(responseBody)
                }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getZahtjeviByKorisnikId(idkor: Int): List<Zahtjev>? {
        val url = "$urlBase/Zahtjev/korid/$idkor"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        return try {
            val response = executeRequest(request)
            if (response.isSuccessful) {
                response.body?.string()?.let { responseBody ->
                    Json.decodeFromString<List<Zahtjev>>(responseBody)
                }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun deleteZahtjev(id: Int): Boolean {
        val url = "$urlBase/Zahtjev/$id"

        val request = Request.Builder()
            .url(url)
            .delete()
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
