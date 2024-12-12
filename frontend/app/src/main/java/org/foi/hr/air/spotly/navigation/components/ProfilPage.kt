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
import androidx.compose.material3.Text
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
import org.foi.hr.air.spotly.data.UserType
import org.foi.hr.air.spotly.data.Vehicle
import org.foi.hr.air.spotly.network.UserService
import org.foi.hr.air.spotly.network.VoziloService
import org.foi.hr.air.spotly.ui.theme.SpotlyTheme

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

    val fetchUserDetails = {
        (context as? ComponentActivity)?.lifecycleScope?.launch {
            try {
                val fetchedUser = UserService.fetchUserId(3) // dohvati prijavljenog korisnika
                user.value = fetchedUser

                fetchedUser?.let {
                    val fetchedUserType = UserService.fetchUserTypeId(it.tipKorisnikaId!!)
                    userType.value = fetchedUserType

                    val fetchedVehicle = VoziloService.fetchVehicleByUserId(it.id)
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Profil korisnika",
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Osvježi podatke")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePagePreview() {
    ProfilePage()
}
