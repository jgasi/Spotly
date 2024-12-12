package org.foi.hr.air.spotly.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.core.vehicle_lookup.VehicleData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleSuccessDialog(
    onDismissRequest: () -> Unit,
    vehicleData: VehicleData
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White)
        ) {
            Text(
                text = "Podaci o vozilu",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            vehicleData.marka?.let {
                Text(text = "Marka: $it", style = MaterialTheme.typography.bodyMedium)
            }
            vehicleData.model?.let {
                Text(text = "Model: $it", style = MaterialTheme.typography.bodyMedium)
            }
            vehicleData.godiste?.let {
                Text(text = "Godište: $it", style = MaterialTheme.typography.bodyMedium)
            }
            vehicleData.registracija?.let {
                Text(text = "Registracija: $it", style = MaterialTheme.typography.bodyMedium)
            }
            vehicleData.tipVozila?.let {
                Text(text = "Tip vozila: ${vehicleData.tipVozila?.tip}", style = MaterialTheme.typography.bodyMedium)
            }
            vehicleData.status?.let {
                Text(text = "Status: $it", style = MaterialTheme.typography.bodyMedium)
            }

            vehicleData.korisnik?.let { user ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Podaci o korisniku",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                user.ime?.let {
                    Text(text = "Ime: $it", style = MaterialTheme.typography.bodyMedium)
                }
                user.prezime?.let {
                    Text(text = "Prezime: $it", style = MaterialTheme.typography.bodyMedium)
                }
                user.email?.let {
                    Text(text = "Email: $it", style = MaterialTheme.typography.bodyMedium)
                }
                user.brojMobitela?.let {
                    Text(text = "Broj mobitela: $it", style = MaterialTheme.typography.bodyMedium)
                }
                user.status?.let {
                    Text(text = "Status: $it", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onDismissRequest) {
                    Text("Zatvori")
                }
            }
        }
    }
}
