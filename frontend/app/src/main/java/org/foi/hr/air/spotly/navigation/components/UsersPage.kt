package org.foi.hr.air.spotly.navigation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.foi.hr.air.spotly.data.User
import org.foi.hr.air.spotly.network.UserService.fetchUserTypes
import org.foi.hr.air.spotly.network.UserService.fetchUsers
import org.foi.hr.air.spotly.network.UserService.registerUser

@Composable
fun UsersPage() {
    var users by remember { mutableStateOf(listOf<User>()) }
    var userTypes by remember { mutableStateOf(mapOf<Int, String>()) }
    val coroutineScope = rememberCoroutineScope()
    var showRegistration by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    fun fetchUserData() {
        coroutineScope.launch {
            isLoading = true
            try {
                users = fetchUsers()
                userTypes = fetchUserTypes()
            } catch (e: Exception) {
                Log.e("UsersPage", "Error fetching users: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        fetchUserData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        UserTableHeader()

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(users) { user ->
                    UserTableRow(
                        user = user,
                        userType = userTypes[user.tipKorisnikaId] ?: "Nepoznat"
                    )
                }
            }
        }

        Button(
            onClick = { showRegistration = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
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
                    try {
                        val success = registerUser(newUser)
                        if (success) {
                            fetchUserData()
                            showRegistration = false
                        }
                    } catch (e: Exception) {
                        Log.e("UsersPage", "Error during registration: ${e.message}")
                    }
                }
            }
        )
    }
}

@Composable
fun UserTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .padding(vertical = 12.dp, horizontal = 8.dp)
    ) {
        TableHeaderCell("Ime", weight = 0.8f)
        TableHeaderCell("Prezime", weight = 0.8f)
        TableHeaderCell("Email", weight = 1.2f)
        TableHeaderCell("Broj\nmobitela", weight = 0.8f)
        TableHeaderCell("Status", weight = 0.6f)
        TableHeaderCell("Tip\nkorisnika", weight = 0.8f)
    }
}

@Composable
fun RowScope.TableHeaderCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .weight(weight)
            .padding(4.dp),
        softWrap = true,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun UserTableRow(user: User, userType: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableRowCell(user.ime, weight = 0.8f)
        TableRowCell(user.prezime, weight = 0.8f)
        TableRowCell(user.email, weight = 1.2f, isEmail = true)
        TableRowCell(user.brojMobitela ?: "-", weight = 0.8f)
        TableRowCell(user.status ?: "-", weight = 0.6f)
        TableRowCell(userType, weight = 0.8f)
    }
}

@Composable
fun RowScope.TableRowCell(
    text: String,
    weight: Float,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .weight(weight)
            .padding(4.dp),
        softWrap = false
    )
}