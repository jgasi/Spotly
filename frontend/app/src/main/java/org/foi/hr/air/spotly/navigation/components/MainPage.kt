import android.content.Context
import android.graphics.ImageDecoder
import android.health.connect.datatypes.units.Length
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.core.vehicle_lookup.VehicleData
import com.example.lookup_manual.*
import com.example.lookup_ocr.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.foi.hr.air.spotly.ReserveParkingSpaceActivity
import org.foi.hr.air.spotly.data.ParkingSpace
import org.foi.hr.air.spotly.data.ParkingSpaceData
import org.foi.hr.air.spotly.KazneScreen
import org.foi.hr.air.spotly.MojiZahtjeviScreen
import org.foi.hr.air.spotly.ProfilePage
import org.foi.hr.air.spotly.QueueScreen
import org.foi.hr.air.spotly.RequestSelectionScreen
<<<<<<< HEAD
import org.foi.hr.air.spotly.UpravljanjeZahtjevimaActivity
=======
import org.foi.hr.air.spotly.UpravljajOdgovorenimZahtjevimaScreen
>>>>>>> develop
import org.foi.hr.air.spotly.UpravljanjeZahtjevimaScreen
import org.foi.hr.air.spotly.data.QueueViewModel
import org.foi.hr.air.spotly.data.UserStore
import org.foi.hr.air.spotly.database.AppDatabase
import org.foi.hr.air.spotly.datastore.RoomVehicleLookupDataSource
import org.foi.hr.air.spotly.navigation.components.SendingDocumentsScreen
import org.foi.hr.air.spotly.navigation.components.*
<<<<<<< HEAD
=======
import org.foi.hr.air.spotly.ui.ParkingAvailabilityPage
import org.foi.hr.air.spotly.network.ApiService
import org.foi.hr.air.spotly.repository.OfflineRepository
>>>>>>> develop
import org.foi.hr.air.spotly.ui.VehicleSuccessDialog
import java.io.BufferedReader
import java.io.InputStreamReader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val selectImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        uri: Uri? ->
        selectedImageUri.value = uri
    }

    val showErrorDialog = remember { mutableStateOf(false) }
    val errorDialogmessage = remember { mutableStateOf("") }

    val showSuccessDialog = remember { mutableStateOf(true) }
    val vehicleData = remember { mutableStateOf<VehicleData?>(null) }

    val currentDestination = navController.currentBackStackEntry?.destination?.route
    if (currentDestination != "login" || UserStore.getUser()?.token.isNullOrEmpty()) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(navController = navController, onClose = {
                    scope.launch { drawerState.close() }
                }, onLogout = onLogout)
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Spotly") },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    NavigationHost(
                        navController,
                        selectImageLauncher = {
                            selectedImageUri.value = null
                            selectImageLauncher.launch("image/*")
                        },
                        selectedImageUri = selectedImageUri.value,
                        onFailedLookup = { reason, statusCode ->
                            errorDialogmessage.value = reason
                            showErrorDialog.value = true
                        },
                        onSuccessfulLookup = { vehicle ->
                            vehicleData.value = vehicle
                            showSuccessDialog.value = true
                        }
                    )
                }

                if (showErrorDialog.value) {
                    BasicAlertDialog(
                        onDismissRequest = { showErrorDialog.value = false },
                        properties = DialogProperties(
                            dismissOnClickOutside = true,
                            usePlatformDefaultWidth = false
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .background(Color.White)
                        ) {
                            Text(
                                text = "Greška",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = errorDialogmessage.value,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = { showErrorDialog.value = false}
                                ) {
                                    Text("Ok")
                                }
                            }
                        }
                    }
                }

                if (showSuccessDialog.value && vehicleData.value != null) {
                    VehicleSuccessDialog(
                        onDismissRequest = { showSuccessDialog.value = false },
                        vehicleData = vehicleData.value!!
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerContent(navController: NavController, onClose: () -> Unit, onLogout: () -> Unit) {
    val user = UserStore.getUser()
    val isAdmin = user?.tipKorisnikaId == 1
    val isZaposlenik = user?.tipKorisnikaId == 2
    val isObicanKorisnik = user?.tipKorisnikaId == 3

    ModalDrawerSheet {
        Text(
            text = "Izbornik",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )
        HorizontalDivider()
        DrawerItem("Početni zaslon", onClick = {
            navController.navigate("homePage")
            onClose()
        })

        if (isAdmin) {
            DrawerItem("Korisnici", onClick = {
                navController.navigate("users")
                onClose()
            })
            DrawerItem("Upravljanje kaznama korisnika", onClick = {
                navController.navigate("brisanjeKazniKorisnika")
                onClose()
            })
            DrawerItem("Upravljanje zahtjevima", onClick = {
                navController.navigate("upravljanjeZahtjevima")
                onClose()
            })
            DrawerItem("Upravljanje odgovorenim zahtjevima", onClick = {
                navController.navigate("upravljajOdgovorenimZahtjevima")
                onClose()
            })
            DrawerItem("Upravljanje zaposleničkim mjestima", onClick = {
                navController.navigate("parkingAvailability")
                onClose()
            })
        }
        if (isZaposlenik){
            DrawerItem("Upravljanje zaposleničkim mjestima", onClick = {
                navController.navigate("parkingAvailability")
                onClose()
            })
        }

        DrawerItem("Profil korisnika", onClick = {
            navController.navigate("userProfile")
            onClose()
        })
        DrawerItem("Slanje dokumenta", onClick = {
            navController.navigate("slanjeDokumenta")
            onClose()
        })

        DrawerItem("Kreiraj zahtjev", onClick = {
            navController.navigate("izborVrsteZahtjeva")
            onClose()
        })
        DrawerItem("Moji zahtjevi", onClick = {
            navController.navigate("mojiZahtjevi")
            onClose()
        })
        DrawerItem("Red čekanja poruka", onClick = {
            navController.navigate("queueScreen")
            onClose()
        })
        DrawerItem("Lokalna baza podataka", onClick = {
            navController.navigate("offlineDatabase")})

        DrawerItem("Statistika", onClick = {
            navController.navigate("statistikaScreen/${user?.id}")
            onClose()
        })

        DrawerItem("Parking", onClick = {
            navController.navigate("parking")
            onClose()
        })

        DrawerItem("Odjava", onClick = {
            onClose()
            onLogout()
        })
    }
}

@Composable
fun DrawerItem(label: String, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(label) },
        selected = false,
        onClick = onClick
    )
}

@Composable
fun NavigationHost(
    navController: NavHostController,
    selectImageLauncher: () -> Unit,
    selectedImageUri: Uri?,
    onFailedLookup: (String, Int?) -> Unit,
    onSuccessfulLookup: (VehicleData) -> Unit
) {
    NavHost(navController = navController, startDestination = "homePage") {
        composable("login") {
            LoginPage(
                navigateToRequestDetails = {
                    navController.navigate("homePage") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("homePage") {
            val context = LocalContext.current
            val db = AppDatabase.getDatabase(context)
            val selectedImageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }

            LaunchedEffect(selectedImageUri) {
                selectedImageUri?.let { uri ->
                    try {
                        val androidBitmap = ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(context.contentResolver, uri)
                        )
                        selectedImageBitmap.value = androidBitmap.asImageBitmap()
                    } catch (e: Exception) {
                        Log.e("DecodeImageError", "Error decoding image: ${e.message}")
                        selectedImageBitmap.value = null
                    }
                }
            }

            HomePage(
                manualLookupHandler = ManualLookupHandler(context, RoomVehicleLookupDataSource(db)),
                ocrLookupHandler = OcrLookupHandler(context, RoomVehicleLookupDataSource(db)),
                onVehicleFetched = { vehicle ->
                    if (vehicle != null) {
                        onSuccessfulLookup(vehicle)
                    }
                },
                onError = { errorMessage, errorStatus ->
                    Log.d("MainPage", "$errorMessage, $errorStatus")
                    onFailedLookup(errorMessage, errorStatus)
                },
                onImageSelected = {
                    selectImageLauncher()
                },
                selectedImageBitmap = selectedImageBitmap,
            )
        }
        composable("userProfile") { ProfilePage() }
        composable("users") { UsersPage(LocalContext.current) }
        composable("queueScreen") {
            val viewModel: QueueViewModel = viewModel()
            QueueScreen(viewModel)
        }
        composable("statistikaScreen/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: 0
            StatistikaScreen(userId)
        }
        composable("slanjeDokumenta") { SendingDocumentsScreen() }
        composable("parking") {

            val context = LocalContext.current
            val parkingSpace = try {
                val inputStream = context.assets.open("parking_spots.json")
                val json = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
                Json.decodeFromString<ParkingSpaceData>(json)
            } catch (e: Exception) {
                Log.e("LoadParkingSpaceData", "Error loading or parsing JSON", e)
                null
            }

            ParkingSpacePage(parkingSpace)

        }
        composable("page3") { PageContent("Page 3") }
        composable("queueScreen") {
            val viewModel: QueueViewModel = viewModel()
            QueueScreen(viewModel)
        }
        composable("brisanjeKazniKorisnika") { KazneScreen() }
        composable("izborVrsteZahtjeva") { RequestSelectionScreen() }
        composable("mojiZahtjevi") { MojiZahtjeviScreen(userId = 2) }
        composable("upravljanjeZahtjevima") { UpravljanjeZahtjevimaScreen() }

        composable("upravljajOdgovorenimZahtjevima") { UpravljajOdgovorenimZahtjevimaScreen() }
        composable("parkingAvailability") { ParkingAvailabilityPage() }
        composable("offlineDatabase") {
            val context = LocalContext.current
            val api = ApiService()
            val db = AppDatabase.getDatabase(context)
            val repository = OfflineRepository(api, db, context)
            OfflineDatabasePage(repository)
        }
    }
}

@Composable
fun PageContent(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge)
    }
}