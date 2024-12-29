package org.foi.hr.air.spotly

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.res.ResourcesCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.foi.hr.air.spotly.data.Kazna
import org.foi.hr.air.spotly.data.ParkingSpace
import org.foi.hr.air.spotly.data.ParkingSpaceData
import org.foi.hr.air.spotly.data.User
import org.foi.hr.air.spotly.data.ZoneList
import org.foi.hr.air.spotly.network.KaznaService.fetchKazneForUser
import org.foi.hr.air.spotly.network.ParkingMjestoService.fetchParkingSpaces
import org.foi.hr.air.spotly.network.UserService.fetchUserTypes
import org.foi.hr.air.spotly.network.UserService.fetchUsers
import org.foi.hr.air.spotly.ui.theme.SpotlyTheme
import java.io.BufferedReader
import java.io.InputStreamReader

class ReserveParkingSpaceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    var scaleFactor by remember { mutableStateOf(1f) }
    val zonesListData = remember {
        mutableStateListOf<ZoneList>().apply {
            parkingSpaceData?.zones?.forEach { (zoneName, zone) ->
                add(ZoneList(zoneName, zone.invalid, zone.charger, zone.invalid, false, false))
            }
        }
    }

    var lastZoneIndex by remember{ mutableStateOf(-1) }

    val imageWidth = 1448f
    val imageHeight = 2048f

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var parkingSpaces by remember { mutableStateOf(listOf<ParkingSpace>()) }

    fun fetchParkingSpaceData() {
        coroutineScope.launch {
            isLoading = true
            try {
                parkingSpaces = withContext(Dispatchers.IO) {
                    fetchParkingSpaces()
                }
            } catch (e: Exception) {
                Log.e("ParkingSpace", "Error fetching parking spaces", e)
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        fetchParkingSpaceData()
    }


    Log.d("ParkingSpace", "Parking space is loaded: ${parkingSpaces}")
    parkingSpaces.forEach { parkingSpace ->
        Log.d("ParkingSpace", "ParkingSpace details: $parkingSpace")
    }

    Log.d("ScreenSize", "Screen width: $screenWidth dp, Screen height: $screenHeight dp")

    val scaleRatioCanvas = (screenWidth) / imageWidth
    Log.d("PolygonZone", "Scale Ratio Canvas: $scaleRatioCanvas")
    Log.d("PolygonZone", "Screen width: $screenWidth dp, Screen height: $screenHeight dp")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(0.dp, 0.dp),
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
                .padding(0.dp, 0.dp)
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
                    } catch (e: Exception) {
                        Log.e("Zoom", "Error occurred during zoom/pan ${e.message}", e)
                    }
                }
                .onGloballyPositioned { coordinates ->
                    val boxWidth = coordinates.size.width
                    val boxHeight = coordinates.size.height
                    scaleFactor = boxWidth.toFloat() / imageWidth
                    Log.d("PolygonZone", "Box width: $boxWidth, Box height: $boxHeight")
                }
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .clipToBounds()
        ) {
            val painter = painterResource(id = R.drawable.garaza_parking_v1)
            Image(
                painter = painter,
                contentDescription = "Zoomable image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
                    .pointerInput(true) {
                        detectTapGestures {

                        }
                    }
                    .zIndex(0f)
            )

            parkingSpaceData?.zones?.forEach { (zoneName, zone) ->
                Log.d("Zone", "Zone is ${zoneName}")
                val zoneLocation = zone.location
                if (zoneLocation.shape == "rect") {
                    zoneLocation.size?.let {
                        val width = it.width.toFloat() * scaleFactor
                        val height = it.height.toFloat() * scaleFactor
                        val position = zoneLocation.position

                        position?.let {
                            val x = it.x.toFloat() * scaleFactor
                            val y = it.y.toFloat() * scaleFactor

                            val density = LocalDensity.current.density
                            val widthDp = (width / density).dp
                            val heightDp = (height / density).dp
                            val xDp = (x / density).dp
                            val yDp = (y / density).dp

                            val zoneInList = zonesListData.find { zoneList -> zoneList.name == zoneName }

                            Canvas(modifier = Modifier
                                .size(widthDp, heightDp)
                                .offset(xDp, yDp)
                                .pointerInput(Unit) {
                                    detectTapGestures(onTap = { tapOffset ->
                                        Log.d("RectangleClick", "Clicked inside rectangle $zoneName!")

                                        val zoneIndex = zonesListData.indexOfFirst { it.name == zoneName }
                                        if (zoneIndex != -1) {
                                            val currentZone = zonesListData[zoneIndex]
                                            val updatedZone = currentZone.copy(isClicked = !currentZone.isClicked)
                                            zonesListData[zoneIndex] = updatedZone

                                            Log.d("RectangleClick", "Zone updated: $updatedZone")
                                            if(lastZoneIndex == -1)
                                            {
                                                lastZoneIndex = zoneIndex
                                            }else{
                                                val lastZone = zonesListData[lastZoneIndex]
                                                val lastUpdatedZone = lastZone.copy(isClicked = false)
                                                zonesListData[lastZoneIndex] = lastUpdatedZone

                                                lastZoneIndex = zoneIndex
                                            }
                                        } else {
                                            Log.d("RectangleClick", "Zone not found: $zoneName")
                                        }
                                    })
                                }
                                .zIndex(1f)) {
                                drawRect(
                                    color = when {
                                        zoneInList?.isClicked == true -> Color.Blue.copy(alpha = 0.5f)
                                        parkingSpaces.getOrNull(zoneName.toInt() - 1)?.status == "slobodno" -> Color.Green.copy(alpha = 0.5f)
                                        else -> Color.Red.copy(alpha = 0.5f)
                                    },
                                    topLeft = Offset(0f, 0f),
                                    size = androidx.compose.ui.geometry.Size(width, height)
                                )
                            }
                        }
                    }
                } else if (zoneLocation.shape == "polygon") {
                    zoneLocation.points?.let { points ->
                        val polygonPoints = points.split(" ").map {
                            val point = it.split(",")
                            val x = point[0].toFloat() * scaleFactor
                            val y = point[1].toFloat() * scaleFactor
                            Pair(x, y)
                        }
                        Log.d("PolygonZone", "Polygon points: $polygonPoints")

                        Canvas(modifier = Modifier
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = { tapOffset ->
                                    val path = Path().apply {
                                        polygonPoints.forEachIndexed { index, point ->
                                            if (index == 0) moveTo(point.first, point.second)
                                            else lineTo(point.first, point.second)
                                        }
                                        close()
                                    }
                                    if (isPointInPolygon(tapOffset, polygonPoints)) {
                                        //TODO: Canvas size, layout offset, polygons not clickable because of canvases overlaying
                                        Log.d("PolygonClick", "Clicked inside polygon!")
                                    }
                                })
                            }) {
                            val path = Path().apply {
                                polygonPoints.forEachIndexed { index, point ->
                                    if (index == 0) moveTo(point.first, point.second)
                                    else lineTo(point.first, point.second)
                                }
                                close()
                            }
                            drawPath(path, Color.Gray.copy(alpha = 0.3f))
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxSize().height(500.dp).zIndex(-2f).background(color = Color.Gray.copy(alpha = 0.4f))
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(20.dp)
            ) {

                Button(
                    onClick = {

                    },
                    Modifier.size(140.dp, 50.dp).padding(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue.copy(alpha = 0.8f))
                ) {

                    Text(text = "Rezerviraj")
                }
            }
        }

    }
}

fun isPointInPolygon(point: Offset, polygon: List<Pair<Float, Float>>): Boolean {
    var isInside = false
    var j = polygon.size - 1
    for (i in polygon.indices) {
        val pi = polygon[i]
        val pj = polygon[j]
        if ((pi.second > point.y) != (pj.second > point.y) &&
            (point.x < (pj.first - pi.first) * (point.y - pi.second) / (pj.second - pi.second) + pi.first)
        ) {
            isInside = !isInside
        }
        j = i
    }
    return isInside
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
