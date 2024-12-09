package com.example.lookup_ocr

import android.graphics.Bitmap
import com.example.core.vehicle_lookup.*
import com.example.core.vehicle_lookup.network.ResponseListener
import com.example.core.vehicle_lookup.network.models.ErrorResponseBody
import com.example.core.vehicle_lookup.network.models.SuccessfulResponseBody
import com.example.ws.network.models.responses.Vehicle
import com.example.ws.request_handlers.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class OcrLookupHandler : LookupHandler {
    override fun handleLookup(licensePlate: String, lookupListner: LookupOutcomeListener) {
        require(licensePlate is String) { "Must receive String instance for 'licensePlate'!" }

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
                        korisnikId = it.korisnikId
                    )
                    lookupListner.onSuccessfulLookup(vehicleData)
                }
            }

            override fun onErrorResponse(error: ErrorResponseBody) {
                lookupListner.onFailedLookup(error.message)
            }

            override fun onNetworkFailiure(t: Throwable) {
                lookupListner.onFailedLookup("Network error: ${t.message}")
            }
        })
    }

    fun extractLicensePlateFromImage(bitmap: Bitmap, onResult: (String?) -> Unit) {
        val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val inputImage = InputImage.fromBitmap(bitmap, 0)

        textRecognizer.process(inputImage)
            .addOnSuccessListener { result ->
                val licensePlate = result.textBlocks
                    .map { it.text }
                    .firstOrNull { it.matches(LICENSE_PLATE_REGEX) }
                onResult(licensePlate)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    companion object {
        private val LICENSE_PLATE_REGEX = Regex("[A-Z]{2}\\d{3,4}[A-Z]{2}")
    }
}