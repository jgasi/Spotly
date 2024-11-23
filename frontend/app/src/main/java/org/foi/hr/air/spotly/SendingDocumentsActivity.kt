package org.foi.hr.air.spotly

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.foi.hr.air.spotly.ui.theme.SpotlyTheme


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

@Composable
fun SendingDocumentsScreen() {
    val context = LocalContext.current
    val selectedPhotoUri = remember { mutableStateOf<Uri?>(null) }
    val capturedPhotoBitmap = remember { mutableStateOf<Bitmap?>(null) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        capturedPhotoBitmap.value = null
        selectedPhotoUri.value = uri
        Toast.makeText(context, "Slika odabrana: $uri", Toast.LENGTH_SHORT).show()
    }

    val captureImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        capturedPhotoBitmap.value = bitmap
        if (bitmap != null) {
            selectedPhotoUri.value = null
            Toast.makeText(context, "Fotografija uspješno snimljena!", Toast.LENGTH_SHORT).show()
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

        selectedPhotoUri.value?.let { uri ->
            val context = LocalContext.current
            val bitmap = remember(uri) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                }
            }

            Column(modifier = Modifier.padding(top = 16.dp)) {
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Selected Photo",
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }

        capturedPhotoBitmap.value?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Captured Photo",
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            )
        }

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
                if (selectedPhotoUri.value != null || capturedPhotoBitmap.value != null) {
                    Toast.makeText(context, "Dokument poslan!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Molimo odaberite dokument prije slanja.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pošaljite dokument")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SendingDocumentsScreenReview() {
    SendingDocumentsScreen()
}
