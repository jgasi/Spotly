package org.foi.hr.air.spotly

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.foi.hr.air.spotly.data.QueueViewModel
import org.foi.hr.air.spotly.data.Zahtjev
import org.foi.hr.air.spotly.network.QueueService

@Composable
fun QueueScreen(viewModel: QueueViewModel) {
    val queue by viewModel.requests.collectAsState(initial = emptyList())  // Pratimo zahtjeve iz ViewModel-a
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Zahtjevi u redu čekanja", style = MaterialTheme.typography.headlineMedium)

        var searchText by remember { mutableStateOf("") }
        OutlinedTextField(
            value = searchText,
            onValueChange = { query ->
                searchText = query
                viewModel.filterQueue(query)
            },
            label = { Text("Pretraži po predmetu") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            singleLine = true
        )

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(queue) { zahtjev ->
                QueueItem(zahtjev) { zahtjevId ->
                    coroutineScope.launch {
                        QueueService.deleteFromQueue(zahtjevId)  // Brisanje zahtjeva
                        viewModel.loadQueue()  // Ponovno učitaj podatke
                    }
                }
            }
        }
    }
}

@Composable
fun QueueItem(zahtjev: Zahtjev, onDelete: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = zahtjev.predmet, style = MaterialTheme.typography.titleLarge)
                Text(text = zahtjev.poruka, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = { onDelete(zahtjev.id) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
