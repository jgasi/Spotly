package org.foi.hr.air.spotly.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vehicle_lookup.viewmodels.VehicleLookupViewModel

@Composable
fun LicensePlatePage(
    viewModel: VehicleLookupViewModel
) {
    val vehicleData by viewModel.vehicleData.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    var licensePlate by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = licensePlate,
            onValueChange = { licensePlate = it },
            label = { Text("Enter License Plate") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.findVehicle(licensePlate) },
            enabled = licensePlate.isNotBlank()
        ) {
            Text("Search Vehicle")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            loading -> CircularProgressIndicator()
            error != null -> Text(text = error ?: "Unknown error", color = MaterialTheme.colorScheme.error)
            vehicleData != null -> VehicleDetailsCard(vehicleData!!)
        }
    }
}

@Composable
fun VehicleDetailsCard(vehicle: com.example.vehicle_lookup.data.Vehicle) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Marka: ${vehicle.marka ?: "Nepoznato"}")
            Text("Model: ${vehicle.model ?: "Nepoznato"}")
            Text("Godina: ${vehicle.godiste ?: "Nepoznato"}")
            Text("Registracija: ${vehicle.registracija ?: "Nepoznato"}")
            Text("Status: ${vehicle.status ?: "Nepoznato"}")
        }
    }
}