package org.foi.hr.air.spotly

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.foi.hr.air.spotly.data.Zahtjev
import org.foi.hr.air.spotly.network.ZahtjevService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DetaljiZahtjevaActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val korisnikId = intent.getIntExtra("korisnik_id", -1)

        setContent {
            DetaljiZahtjevaScreen(korisnikId)
        }
    }
}

@Composable
fun DetaljiZahtjevaScreen(korisnikId: Int) {
    var naslov by remember { mutableStateOf("") }
    var poruka by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Detalji zahtjeva", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Naslov
        Text("Naslov", style = MaterialTheme.typography.bodyLarge)
        TextField(
            value = naslov,
            onValueChange = { naslov = it },
            placeholder = { Text("Unesite naslov") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Poruka", style = MaterialTheme.typography.bodyLarge)
        TextField(
            value = poruka,
            onValueChange = { poruka = it },
            placeholder = { Text("Unesite poruku") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (naslov.isNotBlank() && poruka.isNotBlank()) {
                    isLoading = true
                    println("Početak slanja zahtjeva...")  // Logiraš prije slanja
                    val zahtjev = Zahtjev(
                        id = 0,  // auto increment
                        predmet = naslov,
                        poruka = poruka,
                        odgovor = null,
                        status = "Na čekanju",
                        datumVrijeme = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                        korisnikId = korisnikId,
                        adminId = null,
                        tipZahtjevaId = 2 // hard kodiran jer je ovaj Activity isključivo za kazne
                    )

                    (context as? ComponentActivity)?.lifecycleScope?.launch {
                        try {
                            println("korisnikId: $korisnikId")
                            println("Zahtjev: $zahtjev")

                            val result = ZahtjevService.addZahtjev(zahtjev)
                            isLoading = false
                            if (result) {
                                Toast.makeText(
                                    context,
                                    "Zahtjev uspješno poslan!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Došlo je do greške pri slanju zahtjeva.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            isLoading = false
                            Toast.makeText(
                                context,
                                "Došlo je do greške pri slanju zahtjeva.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                } else {
                    Toast.makeText(
                        context,
                        "Molimo unesite naslov i poruku.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Pošalji zahtjev")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetaljiZahtjevaScreenPreview() {
    DetaljiZahtjevaScreen(korisnikId = 1)
}
