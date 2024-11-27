package org.foi.hr.air.spotly

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class DetaljiZahtjevaActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dohvat ID-a kazne iz Intent-a
        val kaznaId = intent.getIntExtra("kazna_id", -1)

        setContent {
            DetaljiZahtjevaScreen(kaznaId)
        }
    }
}

@Composable
fun DetaljiZahtjevaScreen(kaznaId: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Detalji zahtjeva", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        if (kaznaId != -1) {
            Text("ID kazne: $kaznaId", style = MaterialTheme.typography.bodyLarge)
            // Dodaj dodatne informacije o kazni ako je potrebno
        } else {
            Text("ID kazne nije pronađen.", color = Color.Red)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DetaljiZahtjevaScreenPreview() {
    DetaljiZahtjevaScreen(kaznaId = 1)
}
