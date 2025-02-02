package org.foi.hr.air.spotly

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.foi.hr.air.spotly.data.User
import org.foi.hr.air.spotly.data.UserStore
import org.foi.hr.air.spotly.data.UserType
import org.foi.hr.air.spotly.data.Vehicle
import org.foi.hr.air.spotly.data.Zahtjev
import org.foi.hr.air.spotly.network.UserService
import org.foi.hr.air.spotly.network.VoziloService
import org.foi.hr.air.spotly.network.ZahtjevService
import org.foi.hr.air.spotly.ui.theme.SpotlyTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpotlyTheme {
                ProfilePage()
            }
        }
    }
}

@Composable
fun ProfilePage() {
    val context = LocalContext.current
    val user = remember { mutableStateOf<User?>(null) }
    val userType = remember { mutableStateOf<UserType?>(null) }
    val userVehicle = remember { mutableStateOf<Vehicle?>(null) }
    val showUpdateForm = remember { mutableStateOf(false) }

    val fetchUserDetails = {
        (context as? ComponentActivity)?.lifecycleScope?.launch {
            try {
                val fetchedUser =
                    UserStore.getUser()?.let { UserService.fetchUserId(it.id) } // dohvati prijavljenog korisnika
                user.value = fetchedUser

                fetchedUser.let {
                    val fetchedUserType = it?.let { it1 -> UserService.fetchUserTypeId(it1.id) }
                    userType.value = fetchedUserType

                    val fetchedVehicle = it?.let { it1 -> VoziloService.fetchVehicleByUserId(it1.id) }
                    userVehicle.value = fetchedVehicle
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Greška pri dohvaćanju podataka korisnika.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    if (user.value == null) {
        fetchUserDetails()
    }

    if (showUpdateForm.value) {
        UpdateUserDetailsForm(user = user.value!!, onClose = { showUpdateForm.value = false })
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Profil korisnika",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            user.value?.let {
                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    Text(text = "Ime: ${it.ime}", modifier = Modifier.padding(bottom = 8.dp))
                    Text(text = "Prezime: ${it.prezime}", modifier = Modifier.padding(bottom = 8.dp))
                    Text(text = "Email: ${it.email}", modifier = Modifier.padding(bottom = 8.dp))
                    Text(text = "Broj mobitela: ${it.brojMobitela}", modifier = Modifier.padding(bottom = 8.dp))
                    userType.value?.let {
                        Text(text = "Tip korisnika: ${it.tip}", modifier = Modifier.padding(bottom = 8.dp))
                    }
                    userVehicle.value?.let{
                        Text(text = "Vozilo: ${it.marka} ${it.model}", modifier = Modifier.padding(bottom = 8.dp))
                        Text(text = "Registracija vozila: ${it.registracija}", modifier = Modifier.padding(bottom = 8.dp))
                    }
                }
            } ?: run {
                Text(text = "Nema podataka o korisniku.", modifier = Modifier.padding(bottom = 16.dp))
            }

            Button(
                onClick = { fetchUserDetails() },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Text("Osvježi podatke")
            }

            Button(
                onClick = { showUpdateForm.value = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Izmijeni podatke")
            }
        }
    }
}

@Composable
fun UpdateUserDetailsForm(user: User, onClose: () -> Unit) {
    val context = LocalContext.current
    val ime = remember { mutableStateOf(user.ime) }
    val prezime = remember { mutableStateOf(user.prezime) }
    val brojMobitela = remember { mutableStateOf(user.brojMobitela) }
    val lozinka = remember { mutableStateOf("") }
    val imeChanged = remember { mutableStateOf(false) }
    val prezimeChanged = remember { mutableStateOf(false) }
    val brojMobitelaChanged = remember { mutableStateOf(false) }
    val lozinkaChanged = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Izmjena podataka korisnika", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 16.dp))

        TextField(
            value = user.email,
            onValueChange = {},
            label = { Text("Email") },
            enabled = false,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        TextField(
            value = ime.value,
            onValueChange = {
                ime.value = it
                imeChanged.value = true
            },
            label = { Text("Ime") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        TextField(
            value = prezime.value,
            onValueChange = {
                prezime.value = it
                prezimeChanged.value = true
            },
            label = { Text("Prezime") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        TextField(
            value = brojMobitela.value!!,
            onValueChange = {
                brojMobitela.value = it
                brojMobitelaChanged.value = true
            },
            label = { Text("Broj mobitela") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        TextField(
            value = lozinka.value,
            onValueChange = {
                lozinka.value = it
                lozinkaChanged.value = true
            },
            label = { Text("Lozinka") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                var poruka = if (imeChanged.value) "Ime: ${ime.value}, " else ""
                poruka += if (prezimeChanged.value) "Prezime: ${prezime.value}, " else ""
                poruka += if (brojMobitelaChanged.value) "Broj mobitela: ${brojMobitela.value}, " else ""
                poruka += if (lozinkaChanged.value) "Lozinka: ${lozinka.value}, " else ""

                if(poruka == ""){
                    return@Button
                }

                val zahtjev = Zahtjev(
                    id = 0,
                    predmet = "Izmjena podataka korisnika ${user.id}",
                    poruka = "Korisnik: ${user.id}, ${poruka}",
                    odgovor = null,
                    status = "aktivan",
                    datumVrijeme = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                    adminId = null,
                    korisnikId =  user.id,
                    tipZahtjevaId =  1
                )

                (context as? ComponentActivity)?.lifecycleScope?.launch {
                    try {
                        val result = ZahtjevService.addZahtjev(zahtjev)
                        if (result) {
                            Toast.makeText(context, "Zahtjev poslan!", Toast.LENGTH_SHORT).show()
                            onClose()
                        } else {
                            Toast.makeText(context, "Greška pri slanju zahtjeva.", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context, "Greška pri slanju zahtjeva.", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text("Spremi")
        }

        Button(
            onClick = { onClose() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Odustani")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePagePreview() {
    ProfilePage()
}
