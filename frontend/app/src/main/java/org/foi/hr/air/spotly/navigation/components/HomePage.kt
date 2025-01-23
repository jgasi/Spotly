package org.foi.hr.air.spotly.navigation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import com.example.core.vehicle_lookup.*
import com.example.lookup_manual.*
import com.example.lookup_ocr.*
import org.foi.hr.air.spotly.data.UserStore

@Composable
fun HomePage(
    manualLookupHandler: ManualLookupHandler,
    ocrLookupHandler: OcrLookupHandler,
    onVehicleFetched: (VehicleData) -> Unit,
    onError: (String, Int?) -> Unit,
    onImageSelected: () -> Unit,
    selectedImageBitmap: ImageBitmap?,
) {
    var licensePlate by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(selectedImageBitmap) {
        selectedImageBitmap?.let { bitmap ->
            isLoading = true
            ocrLookupHandler.extractLicensePlateFromImage(bitmap.asAndroidBitmap()) {extractedPlate ->
                if (extractedPlate != null) {
                    ocrLookupHandler.handleLookup(extractedPlate.uppercase(), UserStore.getUser()?.token ?: "", object : LookupOutcomeListener {
                        override fun onSuccessfulLookup(vehicleData: VehicleData) {
                            isLoading = false
                            onVehicleFetched(vehicleData)
                        }

                        override fun onFailedLookup(reason: String, statusCode: Int?) {
                            isLoading = false
                            onError(reason, statusCode)
                        }
                    })
                } else {
                    isLoading = false
                    onError("Nije pronađena registracija na slici", 0)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = licensePlate,
            onValueChange = { licensePlate = it },
            label = { Text("Unesi registarsku oznaku") },
            placeholder = { Text("ZG0000AB") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                manualLookupHandler.handleLookup(licensePlate.uppercase(), UserStore.getUser()?.token ?: "", object : LookupOutcomeListener {
                    override fun onSuccessfulLookup(vehicleData: VehicleData) {
                        isLoading = false
                        onVehicleFetched(vehicleData)
                    }

                    override fun onFailedLookup(errorMessage: String, statusCode: Int?) {
                        isLoading = false
                        onError(errorMessage, statusCode)
                    }
                })
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && licensePlate != ""
        ) {
            Text(if (isLoading) "Učitavanje..." else "Dohvati podatke o vozilu")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onImageSelected()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Slikaj tablice")
        }
    }
}