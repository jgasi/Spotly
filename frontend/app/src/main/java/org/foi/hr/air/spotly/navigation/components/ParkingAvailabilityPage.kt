package org.foi.hr.air.spotly.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.foi.hr.air.spotly.data.ParkingSpace
import org.foi.hr.air.spotly.network.ParkingMjestoService
import org.foi.hr.air.spotly.ui.theme.SpotlyTheme

class ParkingAvailabilityActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpotlyTheme {
                ParkingAvailabilityPage()
            }
        }
    }
}

@Composable
fun ParkingAvailabilityPage() {
    val context = LocalContext.current
    val parkingSpaces = remember { mutableStateOf<List<ParkingSpace>>(emptyList()) }
    val selectedParkingSpace = remember { mutableStateOf<ParkingSpace?>(null) }
    val isButtonEnabled = remember { mutableStateOf(false) }

    val fetchParkingSpaces = {
        (context as? ComponentActivity)?.lifecycleScope?.launch {
            try {
                val fetchedSpaces = ParkingMjestoService.fetchParkingSpaces()
                parkingSpaces.value = fetchedSpaces
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Greška pri dohvaćanju parkirnih mjesta.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    if (parkingSpaces.value.isEmpty()) {
        fetchParkingSpaces()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Parkirna mjesta zaposlenika", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 16.dp))

        DropdownMenuExample(
            items = parkingSpaces.value,
            selectedItem = selectedParkingSpace.value,
            onItemSelected = { space ->
                selectedParkingSpace.value = space
                isButtonEnabled.value = true
            }
        )

        selectedParkingSpace.value?.let {
            Text(text = "Dostupnost: ${it.dostupnost}", modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
        }

        Button(
            onClick = {
                selectedParkingSpace.value?.let { space ->
                    val updatedStatus = if (space.dostupnost == "dostupno") "nedostupno" else "dostupno"
                    val updatedSpace = space.copy(dostupnost = updatedStatus)
                    (context as? ComponentActivity)?.lifecycleScope?.launch {
                        try {
                            val result = ParkingMjestoService.updateParkingSpace(updatedSpace)
                            if (result) {
                                Toast.makeText(context, "Dostupnost ažurirana!", Toast.LENGTH_SHORT).show()
                                fetchParkingSpaces()
                            } else {
                                Toast.makeText(context, "Greška pri ažuriranju dostupnosti.", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(context, "Greška pri ažuriranju dostupnosti.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            enabled = isButtonEnabled.value,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Promijeni dostupnost")
        }
    }
}

@Composable
fun DropdownMenuExample(
    items: List<ParkingSpace>,
    selectedItem: ParkingSpace?,
    onItemSelected: (ParkingSpace) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val label = selectedItem?.let { "Odabrano: ${it.id}" } ?: "Odaberi parkirno mjesto"

    Column {
        Text(
            text = label,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
                .clickable { expanded.value = true }
        )
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    onItemSelected(item)
                    expanded.value = false
                }, text = { Text("ID: ${item.id} - Status: ${item.dostupnost}") })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ParkingAvailabilityPreview() {
    ParkingAvailabilityPage()
}
