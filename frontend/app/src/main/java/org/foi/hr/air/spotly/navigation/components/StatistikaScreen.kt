package org.foi.hr.air.spotly.navigation.components

import ParkingStatistics
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.foi.hr.air.spotly.network.StatistikaService

@Composable
fun StatistikaScreen(userId: Int) {
    val coroutineScope = rememberCoroutineScope()
    var totalKazne by remember { mutableStateOf(0) }
    var userKazne by remember { mutableStateOf(0) }
    var parkingStatistics by remember { mutableStateOf<ParkingStatistics?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                totalKazne = StatistikaService.fetchTotalKazneCount()
                userKazne = StatistikaService.fetchKazneCountByUserId(userId)
                parkingStatistics = StatistikaService.fetchParkingStatistics()
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Statistika Parking Mjesta", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        parkingStatistics?.let { stats ->
            val zauzeta = stats.ukupno - stats.slobodna - stats.rezervirana - stats.blokirana

            Box(
                modifier = Modifier
                    .size(200.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Base circle (total spots)
                CircularProgressIndicator(
                    progress = 1f,
                    modifier = Modifier.size(200.dp),
                    color = Color(0xFF4CAF50), // Slobodno: #4CAF50
                    strokeWidth = 8.dp
                )

                CircularProgressIndicator(
                    progress = (stats.rezervirana.toFloat() + zauzeta.toFloat() + stats.blokirana.toFloat()) / stats.ukupno.toFloat(),
                    modifier = Modifier.size(200.dp),
                    color = Color(0xFF007BFF), // Rezervirano: #007BFF
                    strokeWidth = 8.dp
                )

                CircularProgressIndicator(
                    progress = (zauzeta.toFloat() + stats.blokirana.toFloat()) / stats.ukupno.toFloat(),
                    modifier = Modifier.size(200.dp),
                    color = Color(0xFFFF6F61), // Zauzeto: #FF6F61
                    strokeWidth = 8.dp
                )

                CircularProgressIndicator(
                    progress = stats.blokirana.toFloat() / stats.ukupno.toFloat(),
                    modifier = Modifier.size(200.dp),
                    color = Color(0xFFFFA500), // Blokirano: #FFA500
                    strokeWidth = 8.dp
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${stats.slobodna}/${stats.ukupno}",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Slobodna",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard("Ukupno", stats.ukupno, Color.Gray)
                StatCard("Slobodna", stats.slobodna, Color(0xFF4CAF50)) // Slobodno: #4CAF50
                StatCard("Rezervirana", stats.rezervirana, Color(0xFF007BFF)) // Rezervirano: #007BFF
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard("Zauzeta", zauzeta, Color(0xFFFF6F61)) // Zauzeto: #FF6F61
                StatCard("Blokirana", stats.blokirana, Color(0xFFFFA500)) // Blokirano: #FFA500
            }
        } ?: run {
            CircularProgressIndicator()
            Text("Učitavanje podataka...",
                modifier = Modifier.padding(top = 16.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("Statistika Kazni", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Ukupno kazni: $totalKazne")
        Text("Vaše kazne: $userKazne")

        if (errorMessage != null) {
            Text(
                text = "Greška: $errorMessage",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun StatCard(title: String, value: Int, color: Color) {
    Card(
        modifier = Modifier
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.titleLarge,
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}