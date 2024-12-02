package org.foi.hr.air.spotly

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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.foi.hr.air.spotly.data.Zahtjev
import org.foi.hr.air.spotly.network.ZahtjevService

class UpravljanjeZahtjevimaActivity : ComponentActivity() {

    private var currentPage by mutableStateOf(1)   // Trenutna stranica
    private val pageSize = 5                      // Veličina stranice (koliko zahtjeva na stranici)
    private var zahtjevi by mutableStateOf<List<Zahtjev>>(emptyList())
    private var isLoading by mutableStateOf(false) // Stanje učitavanja

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UpravljanjeZahtjevimaScreen()
        }
    }

    // Pomaknite ovu funkciju iznad `onCreate` kako bi bila dostupna u cijelom razredu.
    @Composable
    fun UpravljanjeZahtjevimaScreen() {
        val context = LocalContext.current

        // Poziv za dohvat zahtjeva kada se stranica promijeni
        LaunchedEffect(currentPage) {
            if (!isLoading) {
                fetchZahtjevi()
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text("Zahtjevi", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            // Prikazivanje liste zahtjeva
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(zahtjevi) { zahtjev ->
                        ZahtjevItem(zahtjev)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigacija između stranica
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = { previousPage() }) {
                    Text("Previous")
                }
                Button(onClick = { nextPage() }) {
                    Text("Next")
                }
            }
        }
    }

    @Composable
    fun ZahtjevItem(zahtjev: Zahtjev) {
        // Prikazivanje pojedinačnog zahtjeva
        Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Naslov: ${zahtjev.predmet}")
                Text("Poruka: ${zahtjev.poruka}")
                Text("Status: ${zahtjev.status}")
            }
        }
    }

    // Funkcija za dohvat zahtjeva na temelju trenutne stranice
    private fun fetchZahtjevi() {
        isLoading = true
        lifecycleScope.launch {
            try {
                val result = ZahtjevService.getPagedZahtjevi(currentPage, pageSize)
                if (result != null) {
                    zahtjevi = result
                } else {
                    Toast.makeText(this@UpravljanjeZahtjevimaActivity, "Greška pri dohvaćanju zahtjeva", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@UpravljanjeZahtjevimaActivity, "Došlo je do greške", Toast.LENGTH_SHORT).show()
            } finally {
                isLoading = false
            }
        }
    }

    // Funkcija za navigaciju na prethodnu stranicu
    private fun previousPage() {
        if (currentPage > 1) {
            currentPage--
        }
    }

    // Funkcija za navigaciju na sljedeću stranicu
    private fun nextPage() {
        currentPage++
    }
}