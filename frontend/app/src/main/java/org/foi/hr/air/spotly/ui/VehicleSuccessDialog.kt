package org.foi.hr.air.spotly.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.core.vehicle_lookup.VehicleData
import kotlinx.coroutines.launch
import org.foi.hr.air.spotly.data.*
import org.foi.hr.air.spotly.network.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleSuccessDialog(
    onDismissRequest: () -> Unit,
    vehicleData: VehicleData
) {
    var reservation by remember { mutableStateOf<Reservation?>(null) }
    var parkingSpace by remember { mutableStateOf<ParkingSpace?>(null) }
    var userTypee by remember { mutableStateOf<UserType?>(null) }
    val korisnikId: Int = vehicleData.korisnik?.id ?: 0
    var showKaznaDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(vehicleData.id) {
        coroutineScope.launch {
            reservation = try {
                ReservationService.fetchReservationByVehicleId(vehicleData.id)
            } catch (e: Exception) {
                null
            }
        }
    }

    LaunchedEffect(reservation?.parkingMjestoId) {
        reservation?.parkingMjestoId?.let { parkingMjestoId ->
            coroutineScope.launch {
                parkingSpace = try {
                    ParkingMjestoService.fetchParkingSpaceById(parkingMjestoId)
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

    LaunchedEffect(korisnikId) {
        coroutineScope.launch {
            userTypee = try {
                UserService.fetchUserTypeByKorisnikId(korisnikId)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun getParkingSpaceTypeName(tipMjestaId: Int): String {
        return when (tipMjestaId) {
            1 -> "Obično"
            2 -> "Zaposlenici"
            3 -> "Električna vozila"
            else -> "Nepoznato"
        }
    }

    fun formatDate(date: String?): String {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val parsedDate = date?.let { formatter.parse(it) }
            val outputFormatter = SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale.getDefault())
            parsedDate?.let { outputFormatter.format(it) } ?: "Nepoznato"
        } catch (e: Exception) {
            "Nepoznato"
        }
    }

    fun validateParking(): Pair<Boolean, String> {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val currentDateTime = formatter.format(now)

        val errorMessages = mutableListOf<String>()

        reservation?.let {
            if (it.datumVrijemeOdlaska != null && it.datumVrijemeOdlaska < currentDateTime) {
                errorMessages.add("Rezervacija je istekla.")
            }
        }

        val userType = userTypee?.tip
        val vehicleType = vehicleData.tipVozila?.tip
        val parkingType = parkingSpace?.tipMjestaId?.let { getParkingSpaceTypeName(it) }

        when (userType) {
            "obican" -> {
                if (!(parkingType == "Obično" || (parkingType == "Električna vozila" && vehicleType == "Električni automobil"))) {
                    errorMessages.add("Korisnik s ulogom '$userType' ne može parkirati na ovom mjestu.")
                }
            }
            "zaposlenik", "admin" -> {
                if (!(parkingType == "Obično" || parkingType == "Zaposlenici" || (parkingType == "Električna vozila" && vehicleType == "Električni automobil"))) {
                    errorMessages.add("Korisnik s ulogom '$userType' ne može parkirati na ovom mjestu.")
                }
            }
            else -> errorMessages.add("Nepoznata uloga korisnika.")
        }

        return if (errorMessages.isEmpty()) {
            Pair(true, "Pravilno parkiran.")
        } else {
            Pair(false, errorMessages.joinToString("\n"))
        }
    }

    val (isValid, message) = validateParking()

    BasicAlertDialog(
        onDismissRequest = {
            onDismissRequest()
            // Reset states to ensure the dialog does not reopen unintentionally
            reservation = null
            parkingSpace = null
            userTypee = null
        },
        properties = DialogProperties(
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Podaci o vozilu",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    vehicleData.marka?.let {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Marka: ")
                                }
                                append(it)
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    vehicleData.model?.let {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Model: ")
                                }
                                append(it)
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    vehicleData.godiste?.let {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Godište: ")
                                }
                                append(it)
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    vehicleData.registracija?.let {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Registracija: ")
                                }
                                append(it)
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    vehicleData.tipVozila?.let {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Tip vozila: ")
                                }
                                append(vehicleData.tipVozila?.tip ?: "")
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    vehicleData.status?.let {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Status: ")
                                }
                                append(it)
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                    vehicleData.korisnik?.let { user ->
                        Text(
                            text = "Podaci o korisniku",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        user.ime?.let {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Ime: ")
                                    }
                                    append(it)
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        user.prezime?.let {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Prezime: ")
                                    }
                                    append(it)
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        userTypee?.tip?.let {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Uloga: ")
                                    }
                                    append(it)
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        user.email?.let {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Email: ")
                                    }
                                    append(it)
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        user.brojMobitela?.let {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Broj mobitela: ")
                                    }
                                    append(it)
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        user.status?.let {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Status: ")
                                    }
                                    append(it)
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                    reservation?.let {
                        Text(
                            text = "Podaci o rezervaciji",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Datum rezervacije: ")
                                }
                                append(formatDate(it.datumVrijemeRezervacije))
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                        it.datumVrijemeOdlaska?.let {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Datum odlaska: ")
                                    }
                                    append(formatDate(it))
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        it.parkingMjestoId?.let {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Parking mjesto: ")
                                    }
                                    append(it.toString())
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } ?: run {
                        Text(text = "Nema podataka o rezervaciji", style = MaterialTheme.typography.bodyMedium)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                    parkingSpace?.let {
                        Text(
                            text = "Podaci o parking mjestu",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Status: ")
                                }
                                append(it.status)
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Dostupnost: ")
                                }
                                append(it.dostupnost)
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                        it.tipMjestaId.let { tipId ->
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Tip mjesta: ")
                                    }
                                    append(tipId?.let { it1 -> getParkingSpaceTypeName(it1) })
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isValid) Color.Green else Color.Red,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Scroll Indicator
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = "Scroll down",
                    tint = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismissRequest,
                        modifier = Modifier
                    ) {
                        Text(text = "Zatvori")
                    }

                    Button(
                        onClick = { showKaznaDialog = true },
                        modifier = Modifier
                    ) {
                        Text(text = "Kreiraj kaznu")
                    }
                }
            }
        }
    }

    val currentUser = UserStore.getUser()

    if (showKaznaDialog) {
        CreateKaznaDialog(
            onDismissRequest = { showKaznaDialog = false },
            onSaveClick = { razlog, novcaniIznos, tipKazne ->
                coroutineScope.launch {
                    try {
                        val kazna = Kazna(
                            id = 0,
                            razlog = razlog,
                            novcaniIznos = novcaniIznos.toString(),
                            datumVrijeme = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                            adminId = currentUser?.id ?: 0,
                            korisnikId = korisnikId,
                            tipKazneId = when (tipKazne) {
                                "Parkiranje u zabranjenoj zoni" -> 1
                                "Nepravilno parkiranje" -> 2
                                else -> 0
                            }
                        )
                        val success = KaznaService.addKazna(kazna)
                        if (success) {
                            Toast.makeText(context, "Kazna uspješno kreirana!", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Greška prilikom kreiranja kazne.", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context, "Greška prilikom kreiranja kazne. An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }
}

@Composable
fun CreateKaznaDialog(onDismissRequest: () -> Unit, onSaveClick: (String, Double, String) -> Unit) {
    var razlog by remember { mutableStateOf("") }
    var novcaniIznos by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Parkiranje u zabranjenoj zoni") }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Kreiraj Kaznu",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = razlog,
                    onValueChange = { razlog = it },
                    label = { Text("Razlog") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = novcaniIznos,
                    onValueChange = { novcaniIznos = it },
                    label = { Text("Novčani Iznos") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Tip kazne:", style = MaterialTheme.typography.bodyMedium)

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        RadioButton(
                            selected = selectedType == "Parkiranje u zabranjenoj zoni",
                            onClick = { selectedType = "Parkiranje u zabranjenoj zoni" }
                        )
                        Text(text = "Parkiranje u zabranjenoj zoni")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        RadioButton(
                            selected = selectedType == "Nepravilno parkiranje",
                            onClick = { selectedType = "Nepravilno parkiranje" }
                        )
                        Text(text = "Nepravilno parkiranje")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismissRequest,
                        modifier = Modifier
                    ) {
                        Text(text = "Zatvori")
                    }

                    Button(
                        onClick = {
                            val iznos = novcaniIznos.toDoubleOrNull() ?: 0.0
                            onSaveClick(razlog, iznos, selectedType)
                            onDismissRequest()
                        },
                        modifier = Modifier
                    ) {
                        Text(text = "Spremi")
                    }
                }
            }
        }
    }
}