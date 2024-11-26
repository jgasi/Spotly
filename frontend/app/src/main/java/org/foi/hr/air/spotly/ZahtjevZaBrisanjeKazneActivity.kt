package org.foi.hr.air.spotly

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.foi.hr.air.spotly.data.Kazna
import org.foi.hr.air.spotly.network.KaznaService

class ZahtjevZaBrisanjeKazneActivity : ComponentActivity() {

    private val kaznaService = KaznaService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZahtjevZaBrisanjeKazneScreen(kaznaService)
        }
    }
}

@Composable
fun ZahtjevZaBrisanjeKazneScreen(kaznaService: KaznaService) {
    var kazne by remember { mutableStateOf<List<Kazna>>(emptyList()) }
    var selectedKazna by remember { mutableStateOf<Kazna?>(null) }
    val context = LocalContext.current

    // Dohvat kazni kada se aktivnost pokrene
    LaunchedEffect(Unit) {
        try {
            val korisnikId = 2 // Zamijeniti s pravim korisničkim ID-om
            kazne = kaznaService.fetchKazneForUser(korisnikId)
        } catch (e: Exception) {
            println("E BO BOEB OEBOE OBE BO EO: " + e.message)
            Toast.makeText(context, "Greška pri dohvaćanju kazni", Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Zahtjev za brisanje kazne", style = MaterialTheme.typography.headlineMedium)

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(kazne) { kazna ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            selectedKazna = kazna
                        },
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Razlog: ${kazna.razlog}")
                        Text("Iznos: ${kazna.novcaniIznos} HRK")
                        Text("Datum: ${kazna.datumVrijeme}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tipka "Dalje" za prelazak na drugi ekran
        Button(
            onClick = {
                if (selectedKazna != null) {
                    // Prosljeđivanje ID-a odabrane kazne prema DetaljiZahtjevaActivity
                    val intent = Intent(context, DetaljiZahtjevaActivity::class.java)
                    intent.putExtra("kazna_id", selectedKazna!!.id) // Prosljeđivanje ID-a
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "Molimo odaberite kaznu!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Dalje")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ZahtjevZaBrisanjeKazneScreenPreview() {
    ZahtjevZaBrisanjeKazneScreen(kaznaService = KaznaService)
}
