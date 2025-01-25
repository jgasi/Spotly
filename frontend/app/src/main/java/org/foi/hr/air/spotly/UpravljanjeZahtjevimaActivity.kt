package org.foi.hr.air.spotly

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.foi.hr.air.spotly.data.Zahtjev
import org.foi.hr.air.spotly.network.ZahtjevService

class UpravljanjeZahtjevimaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UpravljanjeZahtjevimaScreen()
        }
    }
}

@Composable
fun UpravljanjeZahtjevimaScreen() {
    var zahtjevi by remember { mutableStateOf<List<Zahtjev>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Define the launcher for the result
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == ComponentActivity.RESULT_OK) {
            // Refresh the list when returning from OdgovaranjeNaZahtjev
            coroutineScope.launch {
                isLoading = true
                val updatedZahtjevi = withContext(Dispatchers.IO) {
                    ZahtjevService.getZahtjeviNaCekanju()
                }
                if (updatedZahtjevi != null) {
                    zahtjevi = updatedZahtjevi
                } else {
                    Toast.makeText(
                        context,
                        "Greška pri dohvaćanju zahtjeva",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            isLoading = true
            val result = withContext(Dispatchers.IO) {
                ZahtjevService.getZahtjeviNaCekanju()
            }
            if (result != null) {
                zahtjevi = result
            } else {
                Toast.makeText(
                    context,
                    "Greška pri dohvaćanju zahtjeva",
                    Toast.LENGTH_SHORT
                ).show()
            }
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Zahtjevi",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(zahtjevi) { zahtjev ->
                    ZahtjevItem(zahtjev, launcher)
                }
            }
        }
    }
}

@Composable
fun ZahtjevItem(zahtjev: Zahtjev, launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Naslov: ${zahtjev.predmet}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "Poruka: ${zahtjev.poruka}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "Status: ${zahtjev.status}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val intent = Intent(context, OdgovaranjeNaZahtjev::class.java).apply {
                        putExtra("ZAHTJEV_ID", zahtjev.id)
                    }
                    launcher.launch(intent)
                }
            ) {
                Text("Odgovori")
            }
        }
    }
}