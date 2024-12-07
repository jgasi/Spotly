package org.foi.hr.air.spotly.navigation.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.core.vehicle_lookup.*

@Composable
fun HomePage(
    lookupHandler: LookupHandler,
    onVehicleFetched: (VehicleData) -> Unit,
    onError: (String) -> Unit,
    onImageSelected: () -> Unit
) {
    var licensePlate by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

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
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                lookupHandler.handleLookup(licensePlate, object : LookupOutcomeListener {
                    override fun onSuccessfulLookup(vehicleData: VehicleData) {
                        isLoading = false
                        onVehicleFetched(vehicleData)
                    }

                    override fun onFailedLookup(errorMessage: String) {
                        isLoading = false
                        onError(errorMessage)
                    }
                })
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
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
