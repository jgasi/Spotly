package org.foi.hr.air.spotly.network

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.foi.hr.air.spotly.data.Dokumentacija
import org.foi.hr.air.spotly.network.ExecuteService.executeRequest

object DokumentacijaService {
    private const val urlBase = "http://10.0.2.2:5010/api"
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun addDokumentacija(dokumentacija: Dokumentacija): Boolean {
        val url = "$urlBase/Dokumentacija"
        val requestBody = Json.encodeToString(dokumentacija).toRequestBody(jsonMediaType)

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