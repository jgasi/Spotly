package org.foi.hr.air.spotly.navigation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.foi.hr.air.spotly.data.User
import org.foi.hr.air.spotly.network.UserService.fetchUserTypes
import org.foi.hr.air.spotly.network.UserService.fetchUsers
import org.foi.hr.air.spotly.network.UserService.registerUser

@Composable
fun UsersPage() {
    var users by remember { mutableStateOf(listOf<User>())}
    var userTypes by remember { mutableStateOf(mapOf<Int, String>())}
    val coroutineScope = rememberCoroutineScope()
    var showRegistration by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        users = fetchUsers()
        userTypes = fetchUserTypes()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(users.size) { index ->
                val user = users[index]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = user.ime )
                    Text(text = user.prezime )
                    Text(text = user.email )
                    Text(text = user.brojMobitela )
                    Text(text = user.status )
                    Text(text = userTypes[user.tipKorisnikaId] ?: "Nepoznat" )
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