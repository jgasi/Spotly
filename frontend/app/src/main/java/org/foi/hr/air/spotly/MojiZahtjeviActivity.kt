package org.foi.hr.air.spotly

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
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
    var zahtjevi by remember { mutableStateOf<List<Zahtjev>?>(null) }
    var filteredZahtjevi by remember { mutableStateOf<List<Zahtjev>?>(null) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    LaunchedEffect(userId) {
        coroutineScope.launch {
            zahtjevi = withContext(Dispatchers.IO) {
                ZahtjevService.getZahtjeviByKorisnikId(userId)
            }
            filteredZahtjevi = zahtjevi
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        // Search Bar
        BasicTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                filteredZahtjevi = zahtjevi?.filter { zahtjev ->
                    zahtjev.predmet.contains(it.text, ignoreCase = true) ||
                            zahtjev.poruka.contains(it.text, ignoreCase = true)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(50.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(filteredZahtjevi ?: emptyList()) { zahtjev ->
                ZahtjevItem(zahtjev) {
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
                }
            }
        }
    }
}

@Composable
fun ZahtjevItem(zahtjev: Zahtjev, onDelete: () -> Unit) {
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
                Text(text = zahtjev.predmet, style = MaterialTheme.typography.titleLarge)
                Text(text = zahtjev.poruka, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = { onDelete() }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
