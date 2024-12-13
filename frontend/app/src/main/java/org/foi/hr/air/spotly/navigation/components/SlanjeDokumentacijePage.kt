package org.foi.hr.air.spotly.navigation.components

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.foi.hr.air.spotly.data.Dokumentacija
import org.foi.hr.air.spotly.network.DokumentacijaService
import org.foi.hr.air.spotly.ui.theme.SpotlyTheme
import java.io.ByteArrayOutputStream


class SendingDocumentsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpotlyTheme {
                SendingDocumentsScreen()
            }
        }
    }
}

fun convertImageToByteArray(context: Context, uri: Uri): ByteArray? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.readBytes().also {
            inputStream?.close()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    return outputStream.toByteArray()
}

@Composable
fun SendingDocumentsScreen() {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    val imageBytes = remember { mutableStateOf<ByteArray?>(null) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageBytes.value = uri?.let { convertImageToByteArray(context, it) }
        Toast.makeText(context, "Slika odabrana.", Toast.LENGTH_SHORT).show()
    }

    val captureImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            imageBytes.value = convertBitmapToByteArray(bitmap)
            Toast.makeText(context, "Fotografija snimljena.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Fotografija nije snimljena.", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ){
        Text(
            text = "Slanje dokumenata",
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = {
                pickImageLauncher.launch("image/*")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Odaberite dokument")
        }

        Button(
            onClick = {
                captureImageLauncher.launch(null)
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Fotografirajte dokument")
        }

        Button(
            onClick = {
                if(imageBytes.value != null)
                {
                    isLoading = true
                    val dokumentacija = Dokumentacija(
                        id = 0,
                        slika = imageBytes.value,
                        kaznaId = null,
                        zahtjevId = null
                    )

                    (context as? ComponentActivity)?.lifecycleScope?.launch {
                        try {
                            val result = DokumentacijaService.addDokumentacija(dokumentacija)
                            isLoading = false
                            if (result) {
                                Toast.makeText(
                                    context,
                                    "Dokumentacija uspješno poslana!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Došlo je do greške pri slanju dokumentacije.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            isLoading = false
                            Toast.makeText(
                                context,
                                "Došlo je do greške pri slanju dokumentacije.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && imageBytes.value != null
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = "Spremi dokumentaciju")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SendingDocumentsScreenReview() {
    SendingDocumentsScreen()
}
