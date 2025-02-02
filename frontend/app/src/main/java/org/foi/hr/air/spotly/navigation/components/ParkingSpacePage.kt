package org.foi.hr.air.spotly.navigation.components

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.foi.hr.air.spotly.R
import org.foi.hr.air.spotly.data.ParkingSpace
import org.foi.hr.air.spotly.data.ParkingSpaceData
import org.foi.hr.air.spotly.data.UserStore
import org.foi.hr.air.spotly.data.Vehicle
import org.foi.hr.air.spotly.data.ZoneList
import org.foi.hr.air.spotly.isPointInPolygon
import org.foi.hr.air.spotly.network.ParkingMjestoService.blockParkingSpace
import org.foi.hr.air.spotly.network.ParkingMjestoService.fetchParkingSpaces
import org.foi.hr.air.spotly.network.ParkingMjestoService.reserveParkingSpace
import org.foi.hr.air.spotly.network.ParkingMjestoService.unblockParkingSpace
import org.foi.hr.air.spotly.network.UserService
import org.foi.hr.air.spotly.network.VoziloService
import org.foi.hr.air.spotly.network.VoziloService.fetchVehicleByUserId
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun ParkingSpacePage(parkingSpaceData: ParkingSpaceData?) {


    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    var scaleFactor by remember { mutableStateOf(1f) }
    var selectedParkingSpace by remember { mutableStateOf<ParkingSpace?>(null) }
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
    val context = LocalContext.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var parkingSpaces by remember { mutableStateOf(listOf<ParkingSpace>()) }
//    var userVehicle = remember { mutableStateOf<Vehicle?>(null) }



    val user = UserStore.getUser()
//    user.let {
//        val fetchedVehicle = VoziloService.fetchVehicleByUserId(it.id)
//        userVehicle.value = fetchedVehicle
//    }

    var userVehicle = remember { mutableStateOf<Vehicle?>(null) }

    fun fetchUserVehicle() {
        coroutineScope.launch {
            isLoading = true
            try {
                if (user != null) {
                    userVehicle.value = withContext(Dispatchers.IO) {
                        fetchVehicleByUserId(user.id)
                    }
                }
            } catch (e: Exception) {
                Log.e("Vehicle", "Error fetching user vehicle", e)
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }


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
        fetchUserVehicle()
    }


    Log.d("ParkingSpace", "Parking space is loaded: ${parkingSpaces}")
    Log.d("Vehicle", "Vehicle is loaded: ${userVehicle}")
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
                Log.d("Zone", "Zone is ${zone}")
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
                                        parkingSpaces.getOrNull(zoneName.toInt() - 1)?.dostupnost == "Blokirano" -> Color.Gray.copy(alpha = 0.7f)
                                        parkingSpaces.getOrNull(zoneName.toInt() - 1)?.dostupnost == "Slobodno" -> Color.Green.copy(alpha = 0.5f)
                                        parkingSpaces.getOrNull(zoneName.toInt() - 1)?.reservations?.firstOrNull()?.vehicleId == userVehicle.value?.id -> Color.Cyan.copy(alpha = 0.5f)
                                        else -> Color.Red.copy(alpha = 0.5f)
                                    },
                                    topLeft = Offset(0f, 0f),
                                    size = androidx.compose.ui.geometry.Size(width, height)
                                )
                            }
                        }
                    }
                }
            }


            val polygonsData = parkingSpaceData?.zones?.filter { it.value.location.shape == "polygon" }
                ?.map { (zoneName, zone) ->
                    val points = zone.location.points?.split(" ")?.map {
                        val point = it.split(",")
                        val x = point[0].toFloat() * scaleFactor
                        val y = point[1].toFloat() * scaleFactor
                        Pair(x, y)
                    } ?: emptyList()
                    Triple(zoneName, points, zonesListData.find { it.name == zoneName })
                } ?: emptyList()

            val canvasWidth = imageWidth * scaleFactor
            val canvasHeight = imageHeight * scaleFactor
            val density = LocalDensity.current.density
            val canvasWidthWithScreen = canvasWidth / density
            val canvasHeightWithScreen = canvasHeight / density


            Log.d("CanvasSize", "Canvas width ${canvasWidth} and canvas height ${canvasHeight}!")
            Log.d("CanvasSize", "SCREEN: Canvas width ${canvasWidthWithScreen} and canvas height ${canvasHeightWithScreen}!")
            Canvas(modifier = Modifier
//                .fillMaxSize()
                .size(canvasWidthWithScreen.dp, canvasHeightWithScreen.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { tapOffset ->

                        Log.d("PolygonClick", "Clicked inside polygon canvas!")
                        polygonsData.forEachIndexed { index, (zoneName, polygonPoints, _) ->
                            if (isPointInPolygon(tapOffset, polygonPoints)) {
                                val clickedPolygonIndex = zonesListData.indexOfFirst { it.name == zoneName }
                                if (clickedPolygonIndex != -1) {
                                    val currentZone = zonesListData[clickedPolygonIndex]
                                    val updatedZone = currentZone.copy(isClicked = !currentZone.isClicked)
                                    zonesListData[clickedPolygonIndex] = updatedZone

                                    Log.d("PolygonClick", "Clicked inside polygon $zoneName! Zone updated: $updatedZone")

                                    if (lastZoneIndex == -1) {
                                        lastZoneIndex = clickedPolygonIndex
                                    } else {
                                        val lastZone = zonesListData[lastZoneIndex]
                                        val lastUpdatedZone = lastZone.copy(isClicked = false)
                                        zonesListData[lastZoneIndex] = lastUpdatedZone

                                        lastZoneIndex = clickedPolygonIndex
                                    }
                                } else {
                                    Log.d("PolygonClick", "Polygon zone not found: $zoneName")
                                }
                            }
                        }
                    })
                }
            ) {
                polygonsData.forEach { (zoneName, polygonPoints, zoneInList) ->
                    val path = Path().apply {
                        polygonPoints.forEachIndexed { index, point ->
                            if (index == 0) moveTo(point.first, point.second)
                            else lineTo(point.first, point.second)
                        }
                        close()
                    }
                    drawPath(
                        path,
                        color = when {
                            zoneInList?.isClicked == true -> Color.Blue.copy(alpha = 0.5f)
                            parkingSpaces.getOrNull(zoneName.toInt() - 1)?.dostupnost == "Blokirano" -> Color.Gray.copy(alpha = 0.7f)
                            parkingSpaces.getOrNull(zoneName.toInt() - 1)?.dostupnost == "Slobodno" -> Color.Green.copy(alpha = 0.5f)
                            parkingSpaces.getOrNull(zoneName.toInt() - 1)?.reservations?.firstOrNull()?.vehicleId == userVehicle.value?.id -> Color.Cyan.copy(alpha = 0.5f)
                            else -> Color.Red.copy(alpha = 0.5f)
                        },
                    )
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxSize().height(500.dp).zIndex(-2f).background(color = Color.Gray.copy(alpha = 0.4f))
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(20.dp)
            ) {
                if (lastZoneIndex != -1) {
                    val selectedSpace = parkingSpaces.getOrNull(lastZoneIndex)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        when {
                            selectedSpace?.dostupnost == "Blokirano" -> {
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            try {
                                                val result = unblockParkingSpace(lastZoneIndex + 1)
                                                if (result) {
                                                    Toast.makeText(
                                                        context,
                                                        "Parking space unblocked successfully!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    fetchParkingSpaceData()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to unblock parking space.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                                Toast.makeText(
                                                    context,
                                                    "Error unblocking parking space.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    },
                                    Modifier.size(140.dp, 50.dp).padding(0.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green.copy(alpha = 0.8f))
                                ) {
                                    Text(text = "Odblokiraj")
                                }
                            }
                            selectedSpace?.dostupnost == "Slobodno" -> {
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            try {
                                                val result = blockParkingSpace(lastZoneIndex + 1)
                                                if (result) {
                                                    Toast.makeText(
                                                        context,
                                                        "Parking space blocked successfully!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    fetchParkingSpaceData()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to block parking space.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                                Toast.makeText(
                                                    context,
                                                    "Error blocking parking space.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    },
                                    Modifier.size(140.dp, 50.dp).padding(0.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
                                ) {
                                    Text(text = "Blokiraj")
                                }

                                Button(
                                    onClick = {
                                        (context as? ComponentActivity)?.lifecycleScope?.launch {
                                            try {
                                                val reservationStartTime = LocalDateTime.now()
                                                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                                Log.d("RectangleClick", "Zone is ${lastZoneIndex}")
                                                val result = userVehicle.value?.id?.let {
                                                    reserveParkingSpace(
                                                        parkingSpaceId = lastZoneIndex+1,
                                                        voziloId = it,
                                                        reservationStartTime = reservationStartTime,
                                                        reservationEndTime = reservationStartTime
                                                    )
                                                }

                                                if (result == true) {
                                                    Toast.makeText(
                                                        context,
                                                        "Parking space reserved successfully!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    fetchParkingSpaceData()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to reserve parking space.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                                Toast.makeText(
                                                    context,
                                                    "An error occurred while reserving the parking space.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    },
                                    Modifier.size(140.dp, 50.dp).padding(0.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue.copy(alpha = 0.8f))
                                ) {
                                    Text(text = "Rezerviraj")
                                }
                            }
                            else -> {
                                Button(
                                    onClick = {
                                        (context as? ComponentActivity)?.lifecycleScope?.launch {
                                            try {
                                                val reservationStartTime = LocalDateTime.now()
                                                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                                Log.d("RectangleClick", "Zone is ${lastZoneIndex}")
                                                val result = userVehicle.value?.id?.let {
                                                    reserveParkingSpace(
                                                        parkingSpaceId = lastZoneIndex+1,
                                                        voziloId = it,
                                                        reservationStartTime = reservationStartTime,
                                                        reservationEndTime = reservationStartTime
                                                    )
                                                }

                                                if (result == true) {
                                                    Toast.makeText(
                                                        context,
                                                        "Reservation removed successfully!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    fetchParkingSpaceData()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to reserve parking space.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                                Toast.makeText(
                                                    context,
                                                    "An error occurred while reserving the parking space.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    },
                                    Modifier.size(140.dp, 50.dp).padding(0.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue.copy(alpha = 0.8f))
                                ) {
                                    Text(text = "Otkaži rezervaciju")
                                }
                            }
                        }
                    }
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