package uz.otamurod.qrcodescanner.cmp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.otamurod.qrcodescanner.cmp.presentation.screens.permissionrequest.PermissionRequestScreen
import uz.otamurod.qrcodescanner.cmp.ui.theme.DarkColors
import uz.otamurod.qrcodescanner.cmp.ui.theme.LightColors

@Composable
@Preview
fun App() {
    val colors = if (!isSystemInDarkTheme()) LightColors else DarkColors

    MaterialTheme(colorScheme = colors) {
        Navigator(PermissionRequestScreen())
    }
}