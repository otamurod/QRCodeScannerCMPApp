package uz.otamurod.qrcodescanner.cmp.presentation.screens.qrscanner.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun ScannerView(
    modifier: Modifier,
    onScanned: (String) -> Unit
)