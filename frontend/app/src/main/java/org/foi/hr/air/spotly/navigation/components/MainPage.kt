import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lookup_manual.ManualLookupHandler
import com.example.lookup_ocr.OcrLookupHandler
import kotlinx.coroutines.launch
import org.foi.hr.air.spotly.data.Vehicle
import org.foi.hr.air.spotly.navigation.components.HomePage
import org.foi.hr.air.spotly.navigation.components.UsersPage

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
                    selectImageLauncher = {selectImageLauncher.launch("image/*")},
                    selectedImageUri = selectedImageUri.value
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
fun NavigationHost(navController: NavHostController, selectImageLauncher: () -> Unit, selectedImageUri: Uri?) {
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
                    Log.d("MainPage", "Vozilo: $vehicle")
                },
                onError = { errorMessage ->
                    Log.e("MainPage", "Error: $errorMessage")
                },
                onImageSelected = {
                    selectImageLauncher()
                },
                selectedImageBitmap = bitmap,
            )
        }
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