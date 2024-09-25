package uz.otamurod.qrcodescanner.cmp.presentation.screens.qrscanner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import qrcodescannercmpapp.composeapp.generated.resources.Res
import qrcodescannercmpapp.composeapp.generated.resources.camera_placeholder
import qrcodescannercmpapp.composeapp.generated.resources.ic_copy
import uz.otamurod.qrcodescanner.cmp.presentation.screens.qrscanner.component.ScanResultText
import uz.otamurod.qrcodescanner.cmp.presentation.screens.qrscanner.component.ScannerView
import uz.otamurod.qrcodescanner.cmp.ui.theme.copyFeedbackColor
import uz.otamurod.qrcodescanner.cmp.ui.theme.scanResultColor
import kotlin.time.Duration.Companion.seconds

@Composable
fun QRScannerScreen(
    modifier: Modifier = Modifier,
) {
    var scanResult by remember { mutableStateOf("") }
    var isScanning by remember { mutableStateOf(false) }
    var isResultCopied by remember { mutableStateOf(false) }
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Scan the QR Code by placing it within the camera frame",
            textAlign = TextAlign.Center,
            fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
            modifier = Modifier
                .fillMaxWidth()
                .padding()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(250.dp)
                .clip(shape = RoundedCornerShape(size = 14.dp))
                .clipToBounds()
                .border(
                    width = 2.dp,
                    color = Color.Green,
                    shape = RoundedCornerShape(size = 14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            this@Column.AnimatedVisibility(
                visible = !isScanning,
            ) {
                Image(
                    painter = painterResource(Res.drawable.camera_placeholder),
                    contentDescription = "Camera Placeholder",
                )
            }

            this@Column.AnimatedVisibility(
                visible = isScanning,
            ) {
                ScannerView(
                    modifier = Modifier.fillMaxSize(),
                    onScanned = {
                        scanResult = it
                        isResultCopied = false

                        scope.launch {
                            delay(5.seconds)
                            isScanning = false
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                isScanning = !isScanning
                isResultCopied = false
            }
        ) {
            Text(
                text = if (!isScanning) {
                    "Start Scanning"
                } else {
                    "Redo Scanning"
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (scanResult.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 50.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ScanResultText(
                    scanResult = scanResult,
                    onLinkClick = { url ->
                        uriHandler.openUri(url)
                    },
                    scanResultColor = scanResultColor,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    painter = painterResource(Res.drawable.ic_copy),
                    contentDescription = "Copy Scanned QR Code",
                    tint = scanResultColor,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            clipboardManager.setText(AnnotatedString((scanResult)))
                            isResultCopied = true
                        },
                )
            }

            this@Column.AnimatedVisibility(isResultCopied) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Scanned QR Code copied to clipboard",
                    textAlign = TextAlign.Center,
                    color = copyFeedbackColor,
                    fontStyle = MaterialTheme.typography.labelMedium.fontStyle,
                    modifier = Modifier.padding()
                )
            }
        }
    }
}