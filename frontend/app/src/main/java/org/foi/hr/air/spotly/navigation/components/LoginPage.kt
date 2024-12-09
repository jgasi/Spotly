package org.foi.hr.air.spotly.navigation.components

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.foi.hr.air.spotly.network.UserService.login


@Composable
fun LoginPage(navigateToRequestDetails: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var lozinka by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = lozinka,
            onValueChange = { lozinka = it },
            label = { Text("Lozinka") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isEmpty() || lozinka.isEmpty()) {
                    errorMessage = "Sva polja su obavezna!"
                } else {
                    errorMessage = ""
                    isLoading = true
                    coroutineScope.launch {
                        val result = login(email, lozinka)
                        isLoading = false
                        result.onSuccess {
                            errorMessage = "Uspješna prijava!"
                            navigateToRequestDetails()
                        }.onFailure { exception ->
                            errorMessage = exception.message ?: "Došlo je do pogreške!"
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Prijavi se")
            }
        }
    }
}
