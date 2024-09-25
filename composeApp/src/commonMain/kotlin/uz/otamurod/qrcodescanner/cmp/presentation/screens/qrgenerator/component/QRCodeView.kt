package uz.otamurod.qrcodescanner.cmp.presentation.screens.qrgenerator.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun QRCodeView(
    modifier: Modifier = Modifier,
    showProgress: Boolean = true,
    resolutionFactor: Int = 5,
    width: Dp = 240.dp,
    height: Dp = 240.dp,
    value: String
) {
    var qrCodeBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(value) {
        scope.launch {
            withContext(Dispatchers.Default) {
                qrCodeBitmap = try {
                    getImageBitmap(
                        width = (width.value * resolutionFactor).toInt(),
                        height = (height.value * resolutionFactor).toInt(),
                        value = value
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        }
    }

    Box(
        modifier = modifier
            .size(width, height)
    ) {
        qrCodeBitmap?.let { barcode ->
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = BitmapPainter(barcode),
                contentDescription = value
            )
        } ?: run {
            if (showProgress) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

expect fun getImageBitmap(width: Int, height: Int, value: String): ImageBitmap?