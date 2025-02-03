package org.foi.hr.air.spotly

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.foi.hr.air.spotly.data.Kazna
import org.foi.hr.air.spotly.network.KaznaService
import org.foi.hr.air.spotly.ui.theme.SpotlyTheme

class BrisanjeKazniKorisnikaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SpotlyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    KazneScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun KazneScreen(modifier: Modifier = Modifier) {
    var korisnikId by remember { mutableStateOf("") }
    var kazne by remember { mutableStateOf<List<Kazna>?>(null) }
    var filteredKazne by remember { mutableStateOf<List<Kazna>?>(null) }
    var searchText by remember { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf<Kazna?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    fun fetchKazne() {
        if (korisnikId.isNotBlank()) {
            coroutineScope.launch {
                kazne = withContext(Dispatchers.IO) {
                    KaznaService.fetchKazneForUserr(korisnikId.toInt())
                }
                filteredKazne = kazne
            }
        } else {
            Toast.makeText(context, "Unesite korisnički ID", Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(
            value = korisnikId,
            onValueChange = { korisnikId = it },
            label = { Text("Unesite korisnički ID") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true
        )

        Button(
            onClick = { fetchKazne() },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text("Dohvati kazne")
        }

        OutlinedTextField(
            value = searchText,
            onValueChange = { query ->
                searchText = query
                filteredKazne = kazne?.filter { kazna ->
                    kazna.razlog.contains(query, ignoreCase = true)
                }
            },
            label = { Text("Pretraži kazne") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true
        )

        LazyColumn {
            items(filteredKazne ?: emptyList()) { kazna ->
                KaznaItem(kazna, onDelete = {
                    coroutineScope.launch {
                        val success = withContext(Dispatchers.IO) {
                            KaznaService.deleteKazna(kazna.id)
                        }
                        if (success) {
                            kazne = kazne?.filter { it.id != kazna.id }
                            filteredKazne = filteredKazne?.filter { it.id != kazna.id }
                        } else {
                            Toast.makeText(
                                context,
                                "Brisanje kazne nije uspjelo!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }, onEdit = {
                    showEditDialog = kazna
                })
            }
        }
    }

    showEditDialog?.let { kazna ->
        EditKaznaDialog(
            kazna = kazna,
            onDismissRequest = { showEditDialog = null },
            onSaveClick = { updatedKazna ->
                coroutineScope.launch {
                    val success = withContext(Dispatchers.IO) {
                        KaznaService.updateKazna(updatedKazna)
                    }
                    if (success) {
                        kazne = kazne?.map {
                            if (it.id == updatedKazna.id) updatedKazna else it
                        }
                        filteredKazne = filteredKazne?.map {
                            if (it.id == updatedKazna.id) updatedKazna else it
                        }
                        Toast.makeText(
                            context,
                            "Kazna uspješno ažurirana!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Ažuriranje kazne nije uspjelo!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                showEditDialog = null
            }
        )
    }
}

@Composable
fun KaznaItem(kazna: Kazna, onDelete: () -> Unit, onEdit: () -> Unit) {
    val maxLength = 20

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = if (kazna.razlog.length > maxLength) {
                        kazna.razlog.take(maxLength) + "..."
                    } else {
                        kazna.razlog
                    },
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = if (kazna.datumVrijeme.length > maxLength) {
                        kazna.datumVrijeme.take(maxLength) + "..."
                    } else {
                        kazna.datumVrijeme
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row {
                IconButton(onClick = { onEdit() }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = { onDelete() }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

@Composable
fun EditKaznaDialog(
    kazna: Kazna,
    onDismissRequest: () -> Unit,
    onSaveClick: (Kazna) -> Unit
) {
    var razlog by remember { mutableStateOf(kazna.razlog) }
    var novcaniIznos by remember { mutableStateOf(kazna.novcaniIznos) }

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White)
        ) {
            Text(
                text = "Edit Kazna",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = razlog,
                onValueChange = { razlog = it },
                label = { Text("Razlog") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = novcaniIznos,
                onValueChange = { novcaniIznos = it },
                label = { Text("Novčani Iznos") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onDismissRequest, modifier = Modifier.padding(top = 16.dp)) {
                    Text(text = "Zatvori")
                }

                Button(
                    onClick = {
                        val updatedKazna = kazna.copy(razlog = razlog, novcaniIznos = novcaniIznos)
                        onSaveClick(updatedKazna)
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Spremi")
                }
            }
        }
    }
}