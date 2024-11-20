package org.foi.hr.air.spotly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.TextStyle

data class Penalty(val name: String, val reason: String, val amount: String, val date: String)

class DeletePenaltiesRequestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeletePenaltiesRequestScreen()
        }
    }
}

@Composable
fun DeletePenaltiesRequestScreen() {
    var selectedPenalty by remember { mutableStateOf<Penalty?>(null) }
    var textFieldValue by remember { mutableStateOf("Odaberi kaznu") }
    var isListVisible by remember { mutableStateOf(false) }

    // Mock lista kazni, dodat cu kasnije da se spoji na bazu podataka
    val penaltiesList = listOf(
        Penalty("Kazna 1", "Prebrza vožnja", "500 HRK", "2024-11-10 12:00"),
        Penalty("Kazna 2", "Neregistrirano vozilo", "1000 HRK", "2024-10-22 08:00")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Clickable polje za otvaranje liste kazni
        Box(modifier = Modifier.fillMaxWidth().clickable { isListVisible = !isListVisible }) {
            Text(
                text = textFieldValue,
                style = TextStyle(color = Color.Black),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
        }

        // Prikazivanje liste kazni kada je isListVisible true
        if (isListVisible) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(penaltiesList) { penalty ->
                    // Svaka kazna je clickable
                    Text(
                        text = penalty.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedPenalty = penalty
                                textFieldValue = penalty.name
                                isListVisible = false
                            }
                            .padding(8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Prikaz detalja kazne ako je odabrana
        selectedPenalty?.let { penalty ->
            Column {
                Text("Razlog: ${penalty.reason}")
                Text("Iznos: ${penalty.amount}")
                Text("Datum i vrijeme: ${penalty.date}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dalje dugme
        Button(
            onClick = {
                // Korisnik pritisne "dalje" i onda ga odvede na stranicu
                // gdje ce moci upisati opravdanje za kaznu naslov itd i poslati zahtjev
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Dalje")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeletePenaltiesRequestScreenPreview() {
    DeletePenaltiesRequestScreen()
}
