package org.foi.hr.air.spotly

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.foi.hr.air.spotly.data.Zahtjev
import org.foi.hr.air.spotly.network.ZahtjevService

class UpravljanjeZahtjevimaActivity : ComponentActivity() {

    private var currentPage by mutableStateOf(1)
    private val pageSize = 4
    private var zahtjevi by mutableStateOf<List<Zahtjev>>(emptyList())
    private var isLoading by mutableStateOf(false)
    private var hasMoreData by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UpravljanjeZahtjevimaScreen()
        }
    }

    @Composable
    fun UpravljanjeZahtjevimaScreen() {
        val context = LocalContext.current

        LaunchedEffect(currentPage) {
            if (!isLoading) {
                fetchZahtjevi()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Zahtjevi",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(zahtjevi) { zahtjev ->
                        ZahtjevItem(zahtjev)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { previousPage() },
                    enabled = currentPage > 1
                ) {
                    Text("Previous")
                }
                Text(
                    "Page: $currentPage",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Button(
                    onClick = { nextPage() },
                    enabled = hasMoreData
                ) {
                    Text("Next")
                }
            }
        }
    }

    @Composable
    fun ZahtjevItem(zahtjev: Zahtjev) {
        val context = LocalContext.current
        Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Naslov: ${zahtjev.predmet}")
                Text("Poruka: ${zahtjev.poruka}")
                Text("Status: ${zahtjev.status}")
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val intent = Intent(context, OdgovaranjeNaZahtjev::class.java).apply {
                            putExtra("ZAHTJEV_ID", zahtjev.id)
                        }
                        context.startActivity(intent)
                    }
                ) {
                    Text("Odgovori")
                }
            }
        }
    }

    private fun fetchZahtjevi() {
        isLoading = true
        lifecycleScope.launch {
            try {
                val result = ZahtjevService.getPagedZahtjeviNaCekanju(currentPage, pageSize)
                if (result != null) {
                    zahtjevi = result

                    hasMoreData = result.size == pageSize
                } else {
                    Toast.makeText(
                        this@UpravljanjeZahtjevimaActivity,
                        "Greška pri dohvaćanju zahtjeva",
                        Toast.LENGTH_SHORT
                    ).show()
                    hasMoreData = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@UpravljanjeZahtjevimaActivity, "Došlo je do greške", Toast.LENGTH_SHORT).show()
                hasMoreData = false
            } finally {
                isLoading = false
            }
        }
    }

    private fun previousPage() {
        if (currentPage > 1) {
            currentPage--
        }
    }

    private fun nextPage() {
        if (hasMoreData) {
            currentPage++
        }
    }
}
