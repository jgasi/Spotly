import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
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
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.core.vehicle_lookup.VehicleData
import com.example.lookup_manual.*
import com.example.lookup_ocr.*
import kotlinx.coroutines.launch
import org.foi.hr.air.spotly.ProfilePage
import org.foi.hr.air.spotly.navigation.components.*
import org.foi.hr.air.spotly.ui.VehicleSuccessDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage() {
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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController = navController, onClose = {
                scope.launch { drawerState.close() }
            })
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

@Composable
fun DrawerContent(navController: NavController, onClose: () -> Unit) {
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
        DrawerItem("Korisnici", onClick = {
            navController.navigate("users")
            onClose()
        })
        DrawerItem("Profil korisnika", onClick = {
            navController.navigate("userProfile")
            onClose()
        })
        DrawerItem("Page 2", onClick = {
            navController.navigate("page2")
            onClose()
        })
        DrawerItem("Page 3", onClick = {
            navController.navigate("page3")
            onClose()
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
        composable("homePage") {
            val context = LocalContext.current
            val bitmap = remember(selectedImageUri) {
                selectedImageUri?.let { uri ->
                    val androidBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
                    androidBitmap.asImageBitmap()
                }
            }

            HomePage(
                manualLookupHandler = ManualLookupHandler(),
                ocrLookupHandler = OcrLookupHandler(),
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
                selectedImageBitmap = bitmap,
            )
        }
        composable("userProfile") { ProfilePage() }
        composable("users") { UsersPage() }
        composable("page2") { PageContent("Page 2") }
        composable("page3") { PageContent("Page 3") }
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