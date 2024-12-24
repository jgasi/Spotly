package org.foi.hr.air.spotly.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.core.vehicle_lookup.VehicleData
import org.foi.hr.air.spotly.data.ParkingSpace
import org.foi.hr.air.spotly.data.Reservation
import org.foi.hr.air.spotly.network.ParkingMjestoService
import org.foi.hr.air.spotly.network.ReservationService
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleSuccessDialog(
    onDismissRequest: () -> Unit,
    vehicleData: VehicleData
) {
    var reservation by remember { mutableStateOf<Reservation?>(null) }
    var parkingSpace by remember { mutableStateOf<ParkingSpace?>(null) }

    // Fetch reservation data using the vehicle ID
    LaunchedEffect(vehicleData.id) {
        reservation = ReservationService.fetchReservationByVehicleId(vehicleData.id)
    }

    // Fetch parking space data when reservation is available
    LaunchedEffect(reservation?.parkingMjestoId) {
        reservation?.parkingMjestoId?.let { parkingMjestoId ->
            parkingSpace = ParkingMjestoService.fetchParkingSpaceById(parkingMjestoId)
        }
    }

    // Function to get the name of the parking space type based on tipMjestaId
    fun getParkingSpaceTypeName(tipMjestaId: Int): String {
        return when (tipMjestaId) {
            1 -> "Obično"
            2 -> "Zaposlenici"
            3 -> "Električna vozila"
            else -> "Nepoznato"
        }
    }

    // Function to format date in the desired format
    fun formatDate(date: String?): String {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val parsedDate = formatter.parse(date)
            val outputFormatter = SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale.getDefault())
            parsedDate?.let { outputFormatter.format(it) } ?: "Nepoznato"
        } catch (e: Exception) {
            "Nepoznato"
        }
    }

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

            // Display reservation details if available
            reservation?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Podaci o rezervaciji",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(text = "Datum rezervacije: ${formatDate(it.datumVrijemeRezervacije)}", style = MaterialTheme.typography.bodyMedium)
                it.datumVrijemeOdlaska?.let {
                    Text(text = "Datum odlaska: ${formatDate(it)}", style = MaterialTheme.typography.bodyMedium)
                }
                it.parkingMjestoId?.let {
                    Text(text = "Parking mjesto: $it", style = MaterialTheme.typography.bodyMedium)
                }
            } ?: run {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Nema podataka o rezervaciji", style = MaterialTheme.typography.bodyMedium)
            }

            // Display parking space details if available
            parkingSpace?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Podaci o parking mjestu",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(text = "Status: ${it.status}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Dostupnost: ${it.dostupnost}", style = MaterialTheme.typography.bodyMedium)

                // Use the new function to get the name of the parking type
                it.tipMjestaId?.let { tipId ->
                    Text(text = "Tip mjesta: ${getParkingSpaceTypeName(tipId)}", style = MaterialTheme.typography.bodyMedium)
                } ?: run {
                    Text(text = "Tip mjesta: Nepoznato", style = MaterialTheme.typography.bodyMedium)
                }
            } ?: run {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Nema podataka o parking mjestu", style = MaterialTheme.typography.bodyMedium)
            }

            // Close button
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onDismissRequest,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Zatvori")
            }
        }
    }
}
