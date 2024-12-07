package org.foi.hr.air.spotly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.foi.hr.air.spotly.ui.theme.ui.theme.SpotlyTheme

class OdgovaranjeNaZahtjev : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dohvat ID-a iz Intent-a
        val zahtjevId = intent.getIntExtra("ZAHTJEV_ID", -1)

        setContent {
            SpotlyTheme {
                // Prikaz proslijeđenog ID-a
                OdgovaranjeScreen(zahtjevId)
            }
        }
    }
}

@Composable
fun OdgovaranjeScreen(zahtjevId: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Zahtjev ID: $zahtjevId")
    }
}
