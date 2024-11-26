package org.foi.hr.air.spotly

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import org.foi.hr.air.spotly.ui.theme.SpotlyTheme

class IzborVrsteZahtjevaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpotlyTheme {
                RequestSelectionScreen()
            }
        }
    }
}

@Composable
fun RequestSelectionScreen() {
    // Koristimo LocalContext za dohvat konteksta
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(
            text = "Odaberite vrstu zahtjeva",
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Gumb za zahtjev za brisanje kazni
        Button(
            onClick = {
                // Preusmjerenje na aktivnost za brisanje kazni
                val intent = Intent(context, ZahtjevZaBrisanjeKazneActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Zahtjev za brisanje kazni")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RequestSelectionScreenPreview() {
    SpotlyTheme {
        RequestSelectionScreen()
    }
}
