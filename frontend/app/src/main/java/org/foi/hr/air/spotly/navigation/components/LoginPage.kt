package org.foi.hr.air.spotly.navigation.components

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody


@Composable
fun LoginPage(navigateToRequestDetails: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
            value = password,
            onValueChange = { password = it },
            label = { Text("Lozinka") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    errorMessage = "Sva polja su obavezna!"
                } else {
                    errorMessage = ""
                    isLoading = true
                    coroutineScope.launch {
                        try {
                            val client = OkHttpClient()
                            val requestBody = """
                                {
                                    "email": "$email",
                                    "password": "$password"
                                }
                            """.trimIndent().toRequestBody("application/json".toMediaTypeOrNull())

                            val request = Request.Builder()
                                .url("http://localhost:12345/Korisnik/login")
                                .post(requestBody)
                                .build()

                            val response = client.newCall(request).execute()

                            if (response.isSuccessful) {
                                errorMessage = "Uspješna prijava!"
                                navigateToRequestDetails()
                            } else {
                                errorMessage = "Prijava nije uspjela, provjerite podatke i probajte ponovno!"
                            }
                        } catch (e: Exception) {
                            errorMessage = "Error pri spajanju na server: ${e.localizedMessage}"
                        } finally {
                            isLoading = false
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