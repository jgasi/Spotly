package org.foi.hr.air.spotly

import MainPage
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import org.foi.hr.air.spotly.database.AppDatabase
import org.foi.hr.air.spotly.navigation.components.*
import org.foi.hr.air.spotly.navigation.components.LoginPage
import org.foi.hr.air.spotly.navigation.components.UsersPage
import org.foi.hr.air.spotly.network.QueueService
import org.foi.hr.air.spotly.ui.theme.SpotlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        QueueService.init(applicationContext)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpotlyTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginPage(
                            navigateToRequestDetails = { navController.navigate("mainPage") }
                        )
                    }
                    composable("mainPage") {
                        MainPage()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpotlyTheme {
        Greeting("Android")
    }
}