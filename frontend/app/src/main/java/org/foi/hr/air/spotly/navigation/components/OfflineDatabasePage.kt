package org.foi.hr.air.spotly.navigation.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.foi.hr.air.spotly.database.entity.Korisnik
import org.foi.hr.air.spotly.database.entity.Tip_korisnika
import org.foi.hr.air.spotly.repository.OfflineRepository

@Composable
fun OfflineDatabasePage(repository: OfflineRepository) {
    var isOffline by remember { mutableStateOf(false) }
    var korisnici by remember { mutableStateOf<List<Korisnik>>(emptyList()) }
    var tipovi_korisnika by remember { mutableStateOf<List<Tip_korisnika>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        isOffline = repository.isOfflineMode()
        korisnici = repository.getKorisnici()
        tipovi_korisnika = repository.getTipKorisnika()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Offline Mode")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isOffline,
                onCheckedChange = { enabled ->
                    scope.launch {
                        try {
                            repository.setOfflineMode(enabled)
                            isOffline = enabled
                            korisnici = repository.getKorisnici()
                            tipovi_korisnika = repository.getTipKorisnika()
                        } catch (e: Exception) {
                            Log.e("OfflineDatabasePage", "Error toggling offline mode", e)
                        }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Korisnici u offline bazi:", style = MaterialTheme.typography.titleLarge)
        if (korisnici.isNotEmpty()) {
            LazyColumn {
                items(korisnici) { korisnik ->
                    Text(
                        text = "ID: ${korisnik.ID}, Name: ${korisnik.Ime} ${korisnik.Prezime}, Email: ${korisnik.Email}",
                        modifier = Modifier.padding(8.dp)
                    )
                }
                items(tipovi_korisnika) { tip ->
                    Text(
                        text = "ID: ${tip.ID}, Tip: ${tip.Tip}",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        } else {
            Text("Nema podataka.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
