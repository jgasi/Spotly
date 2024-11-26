package org.foi.hr.air.spotly

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import org.foi.hr.air.spotly.data.ParkingSpaceData
import org.foi.hr.air.spotly.ui.theme.SpotlyTheme
import java.io.BufferedReader
import java.io.InputStreamReader

class ReserveParkingSpaceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val assetFiles = assets.list("")?.joinToString(", ")
//        Log.d("Assets", "Files in assets: $assetFiles")

        val parkingSpaceData = loadParkingSpaceData(this)
        setContent {
            SpotlyTheme {
                if (parkingSpaceData != null) {
                    ReserveParkingSpaceScreen(parkingSpaceData)
                }
            }
        }
    }
}


@Composable
fun ReserveParkingSpaceScreen(parkingSpaceData: ParkingSpaceData?) {
    if (parkingSpaceData != null) {
//        for (zone in parkingSpaceData.zones) {
//            Log.d("parkingSpaceData", "Zone number: "+zone.key)
//        }
    }


    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }

    val imageWidth = 1448f
    val imageHeight = 2048f

    Log.d("Zoom", "Scale: $scale, Offset: ${offset.x}, ${offset.y}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(
            text = "Garaža parking",
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
                .pointerInput(Unit) {
                    try {
                        detectTransformGestures { _, pan, zoom, _ ->
                            val newScale = (scale * zoom).coerceIn(1f, 3f)

                            scale = newScale

                            val newOffsetX = offset.x + pan.x
                            val newOffsetY = offset.y + pan.y

                            val maxOffsetX = (scale - 1f) * imageWidth * 0.35f
                            val maxOffsetY = (scale - 1f) * imageHeight * 0.35f

                            val minOffsetX = -maxOffsetX
                            val minOffsetY = -maxOffsetY

                            if (scale < 1.1f) {
                                offset = Offset(0f, 0f)
                            } else {
                                offset = Offset(
                                    x = newOffsetX.coerceIn(minOffsetX, maxOffsetX),
                                    y = newOffsetY.coerceIn(minOffsetY, maxOffsetY)
                                )
                            }
                        }
                    }catch (e: Exception)
                    {
                        Log.e("Zoom", "Error occured during zoom/pan ${e.message}", e)
                    }
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.garaza_parking_v1),
                contentDescription = "Parking Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RectangleShape)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
            )
        }

    }
}

private fun loadParkingSpaceData(context: Context): ParkingSpaceData? {
    return try {
        val inputStream = context.assets.open("parking_spots.json")
        val json = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
        Json.decodeFromString<ParkingSpaceData>(json)
    } catch (e: Exception) {
        Log.e("LoadParkingSpaceData", "Error loading or parsing JSON", e)
        null
    }

}
