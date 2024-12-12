package org.foi.hr.air.spotly

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.foi.hr.air.spotly.data.QueueViewModel
import org.foi.hr.air.spotly.data.Zahtjev

@Composable
fun QueueScreen(viewModel: QueueViewModel) {
    val queue = viewModel.requests.value

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Zahtjevi u redu čekanja", style = MaterialTheme.typography.headlineMedium)
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(queue) { zahtjev ->
                QueueItem(zahtjev)
            }
        }
    }
}

@Composable
fun QueueItem(zahtjev: Zahtjev) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("ID: ${zahtjev.id}", style = MaterialTheme.typography.bodyLarge)
            Text("Status: ${zahtjev.status}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
