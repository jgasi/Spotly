package org.foi.hr.air.spotly

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.platform.LocalContext

class DetaljiZahtjevaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RequestDetailsScreen()
        }
    }
}

@Composable
fun RequestDetailsScreen() {
    // Dohvati kaznu ID iz Intenta
    val penaltyId = (LocalContext.current as? DetaljiZahtjevaActivity)?.intent?.getStringExtra("PENALTY_ID")

    // prema ID-u cu dohvatiti ostale podatke (mock za sada)
    val penaltyDetails = Penalty(
        id = penaltyId ?: "Unknown",
        name = "Kazna $penaltyId",
        reason = "Prebrza vožnja",
        amount = "500 HRK",
        date = "2024-11-10 12:00"
    )

    var subject by remember { mutableStateOf(TextFieldValue("")) }
    var message by remember { mutableStateOf(TextFieldValue("")) }

    val context = LocalContext.current // Dohvati Context iz kompozicije

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Predmet: ${penaltyDetails.name}", style = TextStyle(color = Color.Black), modifier = Modifier.padding(bottom = 8.dp))

        // Input za predmet
        OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            label = { Text("Predmet") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input za poruku
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Poruka") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button za slanje zahtjeva
        Button(
            onClick = {
                // Treba implementirati slanje zahtjeva (trenutno samo ispisuje poruku)
                Toast.makeText(context, "Zahtjev poslan!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pošaljite zahtjev")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RequestDetailsScreenPreview() {
    RequestDetailsScreen()
}
