package org.foi.hr.air.spotly

import MainPage
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.messaging.FirebaseMessaging
import org.foi.hr.air.spotly.navigation.components.LoginPage
import org.foi.hr.air.spotly.network.QueueService
import org.foi.hr.air.spotly.ui.theme.SpotlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        QueueService.init(applicationContext)
        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().subscribeToTopic("weather_alerts")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Subscribed to weather_alerts topic")
                }
            }
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