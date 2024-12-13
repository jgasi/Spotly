package org.foi.hr.air.spotly

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.foi.hr.air.spotly.data.Kazna
import org.foi.hr.air.spotly.network.KaznaService

class ZahtjevZaBrisanjeKazneActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZahtjevZaBrisanjeKazneScreen()
        }
    }
}

@Composable
fun ZahtjevZaBrisanjeKazneScreen() {
    var kazne by remember { mutableStateOf<List<Kazna>>(emptyList()) }
    var filteredKazne by remember { mutableStateOf<List<Kazna>>(emptyList()) }
    var searchText by remember { mutableStateOf("") }
    var selectedKazna by remember { mutableStateOf<Kazna?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Dohvaćanje kazni
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val korisnikId = 2 // Zamijeniti s pravim korisničkim ID-om
                kazne = withContext(Dispatchers.IO) {
                    KaznaService.fetchKazneForUserr(korisnikId)
                } ?: emptyList()  // Ako je rezultat null, koristi praznu listu
                filteredKazne = kazne
            } catch (e: Exception) {
                Toast.makeText(context, "Greška pri dohvaćanju kazni: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Filtriranje kazni prema pretrazi
    LaunchedEffect(searchText) {
        filteredKazne = if (searchText.isEmpty()) {
            kazne
        } else {
            kazne.filter { it.razlog.contains(searchText, ignoreCase = true) }
        }
    }

    // Layout
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Zahtjev za brisanje kazne", style = MaterialTheme.typography.headlineMedium)

        // Polje za pretragu
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Pretraži kazne po razlogu") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Kazne i gumb "Dalje"
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(bottom = 60.dp)) {
                items(filteredKazne) { kazna ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { selectedKazna = kazna },
                        colors = CardDefaults.cardColors(
                            containerColor = if (kazna == selectedKazna) Color.Green else Color.White
                        )
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("Razlog: ${kazna.razlog}")
                            Text("Iznos: ${kazna.novcaniIznos} euro")
                            Text("Datum: ${kazna.datumVrijeme}")
                        }
                    }
                    Divider()
                }
            }

            // Gumb "Dalje" koji je uvijek vidljiv
            Button(
                onClick = {
                    if (selectedKazna != null) {
                        val intent = Intent(context, DetaljiZahtjevaActivity::class.java)
                        intent.putExtra("kazna_id", selectedKazna!!.id)
                        intent.putExtra("korisnik_id", 2)  // Treba staviti ID ulogiranog korisnika
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "Molimo odaberite kaznu!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text("Dalje")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ZahtjevZaBrisanjeKazneScreenPreview() {
    ZahtjevZaBrisanjeKazneScreen()
}
