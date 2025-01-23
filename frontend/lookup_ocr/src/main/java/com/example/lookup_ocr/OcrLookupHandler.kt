package com.example.lookup_ocr

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.core.vehicle_lookup.*
import com.example.core.vehicle_lookup.network.NetworkManager
import com.example.core.vehicle_lookup.network.ResponseListener
import com.example.core.vehicle_lookup.network.models.ErrorResponseBody
import com.example.core.vehicle_lookup.network.models.SuccessfulResponseBody
import com.example.ws.models.responses.User
import com.example.ws.models.responses.VehicleType
import com.example.ws.network.models.responses.Vehicle
import com.example.ws.request_handlers.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OcrLookupHandler(
    private val context: Context,
    private val offlineDataSource: VehicleLookupDataSource
) : LookupHandler {
    private val networkManager = NetworkManager(context)


    override fun handleLookup(licensePlate: String, token: String, lookupListner: LookupOutcomeListener) {
        require(licensePlate is String) { "Must receive String instance for 'licensePlate'!" }

        if (!networkManager.isNetworkAvailable()) {
            tryOfflineLookup(licensePlate, lookupListner)
        } else {
            val requestHandler = GetVehicleByLicensePlateRequestHandler(licensePlate)
            requestHandler.sendRequest(object : ResponseListener<Vehicle> {
                override fun onSuccessfulResponse(response: SuccessfulResponseBody<Vehicle>) {
                    val vehicle = response.data.firstOrNull()
                    vehicle?.let {
                        val vehicleData = VehicleData(
                            id = it.id,
                            marka = it.marka,
                            model = it.model,
                            godiste = it.godiste,
                            registracija = it.registracija,
                            status = it.status,
                            tipVozilaId = it.tipVozilaId,
                            korisnikId = it.korisnikId,
                            korisnik = it.korisnik?.toUserData(),
                            tipVozila = it.tipVozila?.toVehicleTypeData()
                        )
                        lookupListner.onSuccessfulLookup(vehicleData)
                    }
                }

                override fun onErrorResponse(error: ErrorResponseBody) {
                    lookupListner.onFailedLookup(error.message, error.status)
                }

                override fun onNetworkFailiure(t: Throwable) {
                    lookupListner.onFailedLookup("Network error: ${t.message}")
                }
            }, token)
        }
    }

    fun extractLicensePlateFromImage(bitmap: Bitmap, onResult: (String?) -> Unit) {
        val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val inputImage = InputImage.fromBitmap(bitmap, 0)

        textRecognizer.process(inputImage)
            .addOnSuccessListener { result ->
                val recognizedText = result.textBlocks.map {it.text}
                Log.d("OCR", "Recognized text: $recognizedText")

                val cleanedText = recognizedText.map { it.trim().replace("[^A-Z0-9 ]".toRegex(), "") }
                Log.d("OCR", "Cleaned text: $cleanedText")

                val licensePlate = cleanedText
                    .flatMap { text ->
                        LICENSE_PLATE_REGEX.findAll(text).map { match ->
                            val cityCode = match.groups[1]?.value ?: ""
                            val numbers = match.groups[2]?.value ?: ""
                            val letters = match.groups[3]?.value ?: ""
                            "$cityCode$numbers$letters"
                        }.toList()
                    }
                    .firstOrNull()

                Log.d("OCR", "Formatted license plate: $licensePlate")
                onResult(licensePlate)
            }
            .addOnFailureListener { e ->
                Log.e("OCR", "OCR failed: ${e.message}")
                onResult(null)
            }
    }

    companion object {
        private val LICENSE_PLATE_REGEX = Regex("([A-Z]{2})[\\s-]*(\\d{3,4})[\\s-]*([A-Z]{0,2})")
    }

    fun User.toUserData(): UserData {
        return UserData(
            id = this.id,
            ime = this.ime,
            prezime = this.prezime,
            email = this.email,
            brojMobitela = this.brojMobitela,
            status = this.status
        )
    }

    fun VehicleType.toVehicleTypeData(): VehicleTypeData {
        return VehicleTypeData(
            id = this.id,
            tip = this.tip
        )
    }

    private fun tryOfflineLookup(licensePlate: String, lookupListner: LookupOutcomeListener) {
        CoroutineScope(Dispatchers.IO).launch {
            val vehicle = offlineDataSource.getVehicleByLicensePlate(licensePlate)
            if (vehicle != null) {
                withContext(Dispatchers.Main) {
                    lookupListner.onSuccessfulLookup(vehicle)
                }
            } else {
                withContext(Dispatchers.Main) {
                    lookupListner.onFailedLookup("Vozilo nije pronađeno", 404)
                }
            }
        }
    }
}