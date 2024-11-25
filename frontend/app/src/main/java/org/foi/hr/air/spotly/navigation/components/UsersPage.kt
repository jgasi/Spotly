package org.foi.hr.air.spotly.navigation.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.foi.hr.air.spotly.data.User
import org.foi.hr.air.spotly.network.UserService.fetchUserTypes
import org.foi.hr.air.spotly.network.UserService.fetchUsers
import org.foi.hr.air.spotly.network.UserService.registerUser

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp)
    )
}

@Composable
fun UsersPage() {
    var users by remember { mutableStateOf(listOf<User>())}
    var userTypes by remember { mutableStateOf(mapOf<Int, String>())}
    val coroutineScope = rememberCoroutineScope()
    var showRegistration by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            users = fetchUsers()
            userTypes = fetchUserTypes()
        } catch (e: Exception) {
            Log.e("UsersPage", "Error: ${e}")
        }
    }

    val column1Weight = 0.2f
    val column2Weight = 0.2f
    val column3Weight = 0.3f
    val column4Weight = 0.2f
    val column5Weight = 0.1f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                Row(Modifier.background(Color.Gray)) {
                    TableCell(text = "Ime", weight = column1Weight)
                    TableCell(text = "Prezime", weight = column2Weight)
                    TableCell(text = "Email", weight = column3Weight)
                    TableCell(text = "Broj Mobitela", weight = column4Weight)
                    TableCell(text = "Status", weight = column5Weight)
                    TableCell(text = "Tip Korisnika", weight = 0.2f)
                }
            }
            items(users) { user ->
                Row(Modifier.fillMaxWidth()) {
                    TableCell(text = user.ime, weight = column1Weight)
                    TableCell(text = user.prezime, weight = column2Weight)
                    TableCell(text = user.email, weight = column3Weight)
                    TableCell(text = user.brojMobitela ?: "", weight = column4Weight)
                    TableCell(text = user.status ?: "", weight = column5Weight)
                    TableCell(text = userTypes[user.tipKorisnikaId] ?: "Nepoznat", weight = 0.2f)
                }
            }
        }

        Button(
            onClick = { showRegistration = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Dodaj novog korisnika")
        }
    }

    if (showRegistration) {
        RegistrationDialog(
            userTypes = userTypes,
            onDismiss = { showRegistration = false },
            onSubmit = { newUser ->
                coroutineScope.launch {
                    val success = registerUser(newUser)
                    if (success) {
                        users = fetchUsers()
                        showRegistration = false
                    }
                }
            }
        )
    }
}
