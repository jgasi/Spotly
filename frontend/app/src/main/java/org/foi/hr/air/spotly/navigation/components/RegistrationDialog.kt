package org.foi.hr.air.spotly.navigation.components

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import org.foi.hr.air.spotly.data.User
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import org.foi.hr.air.spotly.network.UserService.registerUser

@Composable
fun RegistrationDialog(
    userTypes: Map<Int, String>,
    onDismiss: () -> Unit,
    onSubmit: (User) -> Unit,
) {
    var ime by remember { mutableStateOf("") }
    var prezime by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var lozinka by remember { mutableStateOf("") }
    var brojMobitela by remember { mutableStateOf("") }
    var izabraniTipKorisnika by remember { mutableStateOf(userTypes.keys.firstOrNull() ?: -1) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Dodaj novog korisnika") },
        text = {
            Column {
                TextField(
                    value = ime,
                    onValueChange = { ime = it },
                    label = { Text( "Ime") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = prezime,
                    onValueChange = { prezime = it },
                    label = { Text("Prezime") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = lozinka,
                    onValueChange = { lozinka = it },
                    label = { Text("Lozinka") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                TextField(
                    value = brojMobitela,
                    onValueChange = { brojMobitela = it },
                    label = { Text("Broj mobitela") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { dropdownExpanded = !dropdownExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Izaberite tip korisnika")
                }

                if (dropdownExpanded) {
                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        userTypes.forEach { (id, tip) ->
                            DropdownMenuItem(
                                text = { Text(text = tip) },
                                onClick = {
                                    izabraniTipKorisnika = id
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val user = User(
                        id = 0, // auto increment
                        ime = ime,
                        prezime = prezime,
                        email = email,
                        lozinka = lozinka,
                        brojMobitela = brojMobitela,
                        status = "Aktivan",
                        tipKorisnikaId = izabraniTipKorisnika
                    )
                    coroutineScope.launch {
                        try {
                            val success = registerUser(user)
                            if (success) {
                                onSubmit(user)
                            } else {
                                Toast.makeText(context, "Registracija nije uspjela", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Greška: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            ) {
                Text("Registriraj korisnika")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Odustani")
            }
        }
    )
}
