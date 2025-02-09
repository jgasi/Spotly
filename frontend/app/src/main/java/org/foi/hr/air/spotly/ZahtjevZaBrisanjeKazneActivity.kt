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
import org.foi.hr.air.spotly.data.UserStore
import org.foi.hr.air.spotly.network.KaznaService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
    val currentUser = UserStore.getUser()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val korisnikId = currentUser?.id
                kazne = withContext(Dispatchers.IO) {
                    KaznaService.fetchKazneForUserr(korisnikId)
                } ?: emptyList()
                filteredKazne = kazne
            } catch (e: Exception) {
                Toast.makeText(context, "Greška pri dohvaćanju kazni: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(searchText) {
        filteredKazne = if (searchText.isEmpty()) {
            kazne
        } else {
            kazne.filter { it.razlog.contains(searchText, ignoreCase = true) }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text(
            "Zahtjev za brisanje kazne",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Pretraži kazne po razlogu") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
            ) {
                items(filteredKazne) { kazna ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { selectedKazna = kazna },
                        colors = CardDefaults.cardColors(
                            containerColor = if (kazna == selectedKazna) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Razlog: ${kazna.razlog}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (kazna == selectedKazna) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "Iznos: ${kazna.novcaniIznos} euro",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (kazna == selectedKazna) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "Datum: ${formatDate(kazna.datumVrijeme)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (kazna == selectedKazna) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    if (selectedKazna != null) {
                        val intent = Intent(context, DetaljiZahtjevaActivity::class.java)
                        intent.putExtra("kazna_id", selectedKazna!!.id)
                        intent.putExtra("korisnik_id", currentUser?.id)
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

fun formatDate(dateString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val date = LocalDateTime.parse(dateString, formatter)
    val outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    return date.format(outputFormatter)
}

@Preview(showBackground = true)
@Composable
fun ZahtjevZaBrisanjeKazneScreenPreview() {
    ZahtjevZaBrisanjeKazneScreen()
}