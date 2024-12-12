package org.foi.hr.air.spotly

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
            // Pozivamo novu verziju Composable funkcije
            UpravljanjeZahtjevimaScreen()
        }
    }
}

@Composable
fun UpravljanjeZahtjevimaScreen() {
    var currentPage by remember { mutableStateOf(1) }
    val pageSize = 4
    var zahtjevi by remember { mutableStateOf<List<Zahtjev>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var hasMoreData by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Pokrećemo korutinu za dohvaćanje zahtjeva pri svakoj promjeni stranice
    LaunchedEffect(currentPage) {
        coroutineScope.launch {
            isLoading = true
            val result = withContext(Dispatchers.IO) {
                ZahtjevService.getPagedZahtjeviNaCekanju(currentPage, pageSize)
            }
            if (result != null) {
                zahtjevi = result
                hasMoreData = result.size == pageSize
            } else {
                Toast.makeText(
                    context,
                    "Greška pri dohvaćanju zahtjeva",
                    Toast.LENGTH_SHORT
                ).show()
                hasMoreData = false
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
                    ZahtjevItem(zahtjev)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { if (currentPage > 1) currentPage-- }, // Promjena stranice
                enabled = currentPage > 1
            ) {
                Text("Previous")
            }
            Text(
                "Page: $currentPage",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Button(
                onClick = { if (hasMoreData) currentPage++ }, // Promjena stranice
                enabled = hasMoreData
            ) {
                Text("Next")
            }
        }
    }
}

@Composable
fun ZahtjevItem(zahtjev: Zahtjev) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Naslov: ${zahtjev.predmet}")
            Text("Poruka: ${zahtjev.poruka}")
            Text("Status: ${zahtjev.status}")
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val intent = Intent(context, OdgovaranjeNaZahtjev::class.java).apply {
                        putExtra("ZAHTJEV_ID", zahtjev.id)
                    }
                    context.startActivity(intent)
                }
            ) {
                Text("Odgovori")
            }
        }
    }
}
