package org.foi.hr.air.spotly.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import android.util.Base64

@Composable
fun Base64Image(base64String: String) {
    val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        modifier = Modifier.size(200.dp)
    )
}