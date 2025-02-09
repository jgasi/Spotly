package org.foi.hr.air.spotly

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.foi.hr.air.spotly.data.UserStore
import org.foi.hr.air.spotly.data.Zahtjev
import org.foi.hr.air.spotly.network.ZahtjevService
import org.foi.hr.air.spotly.ui.theme.ui.theme.SpotlyTheme

class OdgovaranjeNaZahtjev : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val zahtjevId = intent.getIntExtra("ZAHTJEV_ID", -1)

        setContent {
            SpotlyTheme {
                OdgovaranjeScreen(zahtjevId) { zahtjev ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val isSuccessful = ZahtjevService.updateZahtjev(zahtjev)
                        withContext(Dispatchers.Main) {
                            val message = if (isSuccessful) "Zahtjev ažuriran!" else "Greška pri ažuriranju!"
                            Toast.makeText(this@OdgovaranjeNaZahtjev, message, Toast.LENGTH_SHORT).show()
                            if (isSuccessful) {
                                setResult(RESULT_OK)
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OdgovaranjeScreen(zahtjevId: Int, onSave: (Zahtjev) -> Unit) {
    var zahtjev by remember { mutableStateOf<Zahtjev?>(null) }
    var odgovor by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf(false) }

    LaunchedEffect(zahtjevId) {
        zahtjev = withContext(Dispatchers.IO) {
            ZahtjevService.getZahtjevById(zahtjevId)
        }
        zahtjev?.let {
            odgovor = it.odgovor ?: ""
            status = it.status
        }
    }

    if (showToast) {
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Nema priloženih dokumenata", Toast.LENGTH_SHORT).show()
            showToast = false
        }
    }

    zahtjev?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Predmet: ${it.predmet}", style = MaterialTheme.typography.headlineSmall)
            Text(text = "Poruka: ${it.poruka}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Datum: ${it.datumVrijeme}", style = MaterialTheme.typography.bodyMedium)

            Text(text = "Odgovor: ", style = MaterialTheme.typography.headlineSmall)

            BasicTextField(
                value = odgovor,
                onValueChange = { odgovor = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    ) {
                        if (odgovor.isEmpty()) {
                            Text(
                                text = "Unesite odgovor...",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Text(text = "Status: ", style = MaterialTheme.typography.headlineSmall)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { status = "Na čekanju" }
                ) {
                    RadioButton(
                        selected = status == "Na čekanju",
                        onClick = { status = "Na čekanju" }
                    )
                    Text(text = "Na čekanju", style = MaterialTheme.typography.bodyLarge)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { status = "Odgovoren" }
                ) {
                    RadioButton(
                        selected = status == "Odgovoren",
                        onClick = { status = "Odgovoren" }
                    )
                    Text(text = "Odgovoren", style = MaterialTheme.typography.bodyLarge)
                }
            }

            Button(
                onClick = { showToast = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Pregledaj dokumente")
            }

            val currentUser = UserStore.getUser()
            Log.d("OdgovaranjeScreen", "Current User: $currentUser")

            Button(
                onClick = {
                    val updatedZahtjev = it.copy(odgovor = odgovor, status = status, adminId = currentUser?.id)
                    Log.d("OdgovaranjeScreen", "Updated Zahtjev: $updatedZahtjev")
                    onSave(updatedZahtjev)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Spremi")
            }
        }
    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}