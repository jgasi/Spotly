package org.foi.hr.air.spotly.navigation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*
import androidx.compose.material3.*
import com.example.core.vehicle_lookup.LookupHandler
import com.example.core.vehicle_lookup.LookupOutcomeListener
import com.example.core.vehicle_lookup.VehicleData
import com.example.core.vehicle_lookup.network.ResponseListener
import com.example.core.vehicle_lookup.network.models.ErrorResponseBody
import com.example.core.vehicle_lookup.network.models.SuccessfulResponseBody
import com.example.ws.request_handlers.GetVehicleByLicensePlateRequestHandler
import org.foi.hr.air.spotly.data.Vehicle

@Composable
fun HomePage(
    lookupHandler: LookupHandler,
    onVehicleFetched: (VehicleData) -> Unit,
    onError: (String) -> Unit
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
    }
}
