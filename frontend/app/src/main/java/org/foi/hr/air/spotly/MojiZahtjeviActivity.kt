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
import androidx.compose.material.icons.filled.Visibility
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

class MojiZahtjeviActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SpotlyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MojiZahtjeviScreen(
                        userId = 2, // hard kodirano dok se ne spoji s login
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MojiZahtjeviScreen(userId: Int, modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Na čekanju", "Odgovoren")

    Column(modifier = modifier.padding(16.dp)) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        when (selectedTab) {
            0 -> ZahtjeviNaCekanjuTab(userId)
            1 -> ZahtjeviOdgovorenTab(userId)
        }
    }
}

@Composable
fun ZahtjeviNaCekanjuTab(userId: Int) {
    var zahtjevi by remember { mutableStateOf<List<Zahtjev>?>(null) }
    var filteredZahtjevi by remember { mutableStateOf<List<Zahtjev>?>(null) }
    var searchText by remember { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf<Zahtjev?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(userId) {
        coroutineScope.launch {
            zahtjevi = withContext(Dispatchers.IO) {
                ZahtjevService.getZahtjeviByKorisnikId(userId)?.filter { it.status == "Na čekanju" }
            }
            filteredZahtjevi = zahtjevi
        }
    }

    Column {
        OutlinedTextField(
            value = searchText,
            onValueChange = { query ->
                searchText = query
                filteredZahtjevi = zahtjevi?.filter { zahtjev ->
                    zahtjev.predmet.contains(query, ignoreCase = true)
                }
            },
            label = { Text("Pretraži po predmetu") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true
        )

        LazyColumn {
            items(filteredZahtjevi ?: emptyList()) { zahtjev ->
                ZahtjevItem(
                    zahtjev = zahtjev,
                    onDelete = {
                        coroutineScope.launch {
                            val success = withContext(Dispatchers.IO) {
                                ZahtjevService.deleteZahtjev(zahtjev.id)
                            }
                            if (success) {
                                zahtjevi = zahtjevi?.filter { it.id != zahtjev.id }
                                filteredZahtjevi = filteredZahtjevi?.filter { it.id != zahtjev.id }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Brisanje zahtjeva nije uspjelo!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    onEdit = {
                        showEditDialog = zahtjev
                    },
                    onViewResponse = {}
                )
            }
        }
    }

    showEditDialog?.let { zahtjev ->
        EditZahtjevDialog(
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
fun ZahtjeviOdgovorenTab(userId: Int) {
    var zahtjevi by remember { mutableStateOf<List<Zahtjev>?>(null) }
    var filteredZahtjevi by remember { mutableStateOf<List<Zahtjev>?>(null) }
    var searchText by remember { mutableStateOf("") }
    var showResponseDialog by remember { mutableStateOf<Zahtjev?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(userId) {
        coroutineScope.launch {
            zahtjevi = withContext(Dispatchers.IO) {
                ZahtjevService.getZahtjeviByKorisnikId(userId)?.filter { it.status == "Odgovoren" }
            }
            filteredZahtjevi = zahtjevi
        }
    }

    Column {
        OutlinedTextField(
            value = searchText,
            onValueChange = { query ->
                searchText = query
                filteredZahtjevi = zahtjevi?.filter { zahtjev ->
                    zahtjev.predmet.contains(query, ignoreCase = true)
                }
            },
            label = { Text("Pretraži po predmetu") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true
        )

        LazyColumn {
            items(filteredZahtjevi ?: emptyList()) { zahtjev ->
                ZahtjevItem(
                    zahtjev = zahtjev,
                    onDelete = {
                        coroutineScope.launch {
                            val success = withContext(Dispatchers.IO) {
                                ZahtjevService.deleteZahtjev(zahtjev.id)
                            }
                            if (success) {
                                zahtjevi = zahtjevi?.filter { it.id != zahtjev.id }
                                filteredZahtjevi = filteredZahtjevi?.filter { it.id != zahtjev.id }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Brisanje zahtjeva nije uspjelo!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    onEdit = {},
                    onViewResponse = {
                        showResponseDialog = zahtjev
                    }
                )
            }
        }
    }

    showResponseDialog?.let { zahtjev ->
        ViewResponseDialog(
            zahtjev = zahtjev,
            onDismissRequest = { showResponseDialog = null }
        )
    }
}

@Composable
fun ZahtjevItem(zahtjev: Zahtjev, onDelete: () -> Unit, onEdit: () -> Unit, onViewResponse: () -> Unit) {
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
            }
            Row {
                if (zahtjev.status == "Na čekanju") {
                    IconButton(onClick = { onEdit() }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                } else if (zahtjev.status == "Odgovoren") {
                    IconButton(onClick = { onViewResponse() }) {
                        Icon(Icons.Default.Visibility, contentDescription = "View Response")
                    }
                }
                IconButton(onClick = { onDelete() }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

@Composable
fun EditZahtjevDialog(
    zahtjev: Zahtjev,
    onDismissRequest: () -> Unit,
    onSaveClick: (Zahtjev) -> Unit
) {
    var predmet by remember { mutableStateOf(zahtjev.predmet) }
    var poruka by remember { mutableStateOf(zahtjev.poruka) }

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
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = poruka,
                onValueChange = { poruka = it },
                label = { Text("Poruka") },
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
                        val updatedZahtjev = zahtjev.copy(predmet = predmet, poruka = poruka)
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

@Composable
fun ViewResponseDialog(
    zahtjev: Zahtjev,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White)
        ) {
            Text(
                text = "Odgovor na Zahtjev",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Predmet: ${zahtjev.predmet}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Poruka: ${zahtjev.poruka}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Odgovor: ${zahtjev.odgovor}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = onDismissRequest,
                modifier = Modifier.align(Alignment.End).padding(top = 16.dp)
            ) {
                Text(text = "Zatvori")
            }
        }
    }
}