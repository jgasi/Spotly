package org.foi.hr.air.spotly.navigation.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.foi.hr.air.spotly.repository.*
import org.foi.hr.air.spotly.ui.Base64Image

@Composable
fun OfflineDatabasePage(repository: OfflineRepository) {
    var isOffline by remember { mutableStateOf(false) }
    var allData by remember { mutableStateOf(AllData(emptyList(), emptyList(), emptyList())) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Offline Mode")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isOffline,
                onCheckedChange = { enabled ->
                    scope.launch {
                        if (enabled) {
                            repository.setOfflineMode(true)

                            allData = repository.getAll()
                        } else {
                            repository.setOfflineMode(false)
                            repository.deleteAll()
                            allData = AllData(emptyList(), emptyList(), emptyList())

                            if (repository.isDbEmpty()) {
                                Log.d("OfflineDatabasePage", "Prazno!")
                            } else {
                                Log.d("OfflineDatabasePage", "Imam podatke!")
                            }

                        }
                        isOffline = enabled
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Podaci u offline bazi:", style = MaterialTheme.typography.titleLarge)
        if (allData.korisnici.isNotEmpty()) {
            LazyColumn {
                items(allData.korisnici) { korisnik ->
                    Text(
                        text = "ID: ${korisnik.ID}, Name: ${korisnik.Ime} ${korisnik.Prezime}, Email: ${korisnik.Email}",
                        modifier = Modifier.padding(8.dp)
                    )
                }
                items(allData.tipovi_korisnika) { tip ->
                    Text(
                        text = "ID: ${tip.ID}, Tip: ${tip.Tip}",
                        modifier = Modifier.padding(8.dp)
                    )
                }
                items(allData.dokumentacija) { dok ->
                    Text(
                        text = "ID: ${dok.ID}, Slika: ${dok.Slika?.let { Base64Image(it) }}, KorisnikId: ${dok.KorisnikID ?: null}, ZahtjevId: ${dok.ZahtjevId ?: null}",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        } else {
            Text("Nema podataka.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
