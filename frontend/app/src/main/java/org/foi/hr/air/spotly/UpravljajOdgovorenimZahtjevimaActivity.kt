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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.foi.hr.air.spotly.data.Zahtjev
import org.foi.hr.air.spotly.network.ZahtjevService
import org.foi.hr.air.spotly.ui.theme.SpotlyTheme

class UpravljajOdgovorenimZahtjevimaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SpotlyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UpravljajOdgovorenimZahtjevimaScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun UpravljajOdgovorenimZahtjevimaScreen(modifier: Modifier = Modifier) {
    var zahtjevi by remember { mutableStateOf<List<Zahtjev>?>(null) }
    var filteredZahtjevi by remember { mutableStateOf<List<Zahtjev>?>(null) }
    var searchText by remember { mutableStateOf("") }
    var currentPage by remember { mutableStateOf(1) }
    val pageSize = 3
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var showEditDialog by remember { mutableStateOf<Zahtjev?>(null) }


    LaunchedEffect(currentPage) {
        coroutineScope.launch {
            zahtjevi = withContext(Dispatchers.IO) {
                ZahtjevService.getPagedZahtjeviOdgovoreni(currentPage, pageSize)
            }
            filteredZahtjevi = zahtjevi
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { query ->
                    searchText = query
                    filteredZahtjevi = zahtjevi?.filter { zahtjev ->
                        zahtjev.predmet.contains(query, ignoreCase = true) ||
                                zahtjev.poruka.contains(query, ignoreCase = true) ||
                                (zahtjev.odgovor?.contains(query, ignoreCase = true) ?: false)
                    }
                },
                label = { Text("Pretraži po predmetu, poruci ili odgovoru") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(filteredZahtjevi ?: emptyList()) { zahtjev ->
                    ZahtjevItem(
                        zahtjev = zahtjev,
                        onEdit = {
                            coroutineScope.launch {
                                showEditDialog = zahtjev
                            }
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { if (currentPage > 1) currentPage-- },
                enabled = currentPage > 1
            ) {
                Text("Prethodna")
            }
            Button(
                onClick = { currentPage++ },
                enabled = zahtjevi?.size == pageSize
            ) {
                Text("Sljedeća")
            }
        }
    }


    showEditDialog?.let { zahtjev ->
        EditZahtjevDialogUpravljaj(
            zahtjev = zahtjev,
            onDismissRequest = { showEditDialog = null },
            onSaveClick = { updatedZahtjev ->
                coroutineScope.launch {
                    val success = withContext(Dispatchers.IO) {
                        ZahtjevService.updateZahtjev(updatedZahtjev)
                    }
                    if (success) {
                        zahtjevi = zahtjevi?.map {
                            if (it.id == updatedZahtjev.id) updatedZahtjev else it
                        }
                        filteredZahtjevi = filteredZahtjevi?.map {
                            if (it.id == updatedZahtjev.id) updatedZahtjev else it
                        }
                        Toast.makeText(
                            context,
                            "Zahtjev uspješno ažuriran!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Ažuriranje zahtjeva nije uspjelo!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        )
    }
}

@Composable
fun ZahtjevItem(zahtjev: Zahtjev, onEdit: () -> Unit) {
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
                    text = if (zahtjev.predmet.length > maxLength) {
                        zahtjev.predmet.take(maxLength) + "..."
                    } else {
                        zahtjev.predmet
                    },
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = if (zahtjev.poruka.length > maxLength) {
                        zahtjev.poruka.take(maxLength) + "..."
                    } else {
                        zahtjev.poruka
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = if (zahtjev.odgovor?.length ?: 0 > maxLength) {
                        zahtjev.odgovor?.take(maxLength) + "..."
                    } else {
                        zahtjev.odgovor ?: ""
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = { onEdit() }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        }
    }
}

@Composable
fun EditZahtjevDialogUpravljaj(
    zahtjev: Zahtjev,
    onDismissRequest: () -> Unit,
    onSaveClick: (Zahtjev) -> Unit
) {
    var predmet by remember { mutableStateOf(zahtjev.predmet) }
    var poruka by remember { mutableStateOf(zahtjev.poruka) }
    var odgovor by remember { mutableStateOf(zahtjev.odgovor ?: "") }
    var status by remember { mutableStateOf(zahtjev.status) }

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White)
        ) {
            Text(
                text = "Edit Zahtjev",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = predmet,
                onValueChange = { predmet = it },
                label = { Text("Predmet") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )

            OutlinedTextField(
                value = poruka,
                onValueChange = { poruka = it },
                label = { Text("Poruka") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                readOnly = true
            )

            OutlinedTextField(
                value = odgovor,
                onValueChange = { odgovor = it },
                label = { Text("Odgovor") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            OutlinedTextField(
                value = status,
                onValueChange = { status = it },
                label = { Text("Status") },
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
                        val updatedZahtjev = zahtjev.copy(predmet = predmet, poruka = poruka, odgovor = odgovor, status = status)
                        onSaveClick(updatedZahtjev)
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Spremi")
                }
            }
        }
    }
}