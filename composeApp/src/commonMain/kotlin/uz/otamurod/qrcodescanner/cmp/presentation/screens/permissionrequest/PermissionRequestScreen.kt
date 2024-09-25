package uz.otamurod.qrcodescanner.cmp.presentation.screens.permissionrequest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.jetbrains.compose.resources.painterResource
import qrcodescannercmpapp.composeapp.generated.resources.Res
import qrcodescannercmpapp.composeapp.generated.resources.qr_code_placeholder
import uz.otamurod.qrcodescanner.cmp.presentation.screens.main.MainScreen

class PermissionRequestScreen : Screen {
    @Composable
    override fun Content() {
        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) {
            factory.createPermissionsController()
        }

        BindEffect(controller)

        val viewModel = viewModel {
            PermissionRequestViewModel(controller)
        }

        val lifecycleOwner = LocalLifecycleOwner.current
        var isCheckingPermission by remember { mutableStateOf(false) }

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME && isCheckingPermission) {
                    viewModel.checkCameraPermissionState()
                    isCheckingPermission = false
                }
            }

            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (viewModel.state) {
                PermissionState.Granted -> {
                    Navigator(MainScreen())
                }

                PermissionState.DeniedAlways -> {
                    Text(
                        text = "Permission was permanently declined.",
                        textAlign = TextAlign.Center,
                        fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                        modifier = Modifier
                            .padding(top = 76.dp)
                            .fillMaxWidth()
                            .padding()
                    )

                    Button(
                        onClick = {
                            isCheckingPermission = true
                            controller.openAppSettings()
                        }
                    ) {
                        Text("Open app settings")
                    }
                }

                else -> {
                    Text(
                        text = "QR Code Scanner App",
                        textAlign = TextAlign.Center,
                        fontStyle = MaterialTheme.typography.displayLarge.fontStyle,
                        modifier = Modifier
                            .padding(top = 56.dp)
                            .fillMaxWidth()
                            .padding()
                    )

                    Image(
                        painter = painterResource(Res.drawable.qr_code_placeholder),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .size(240.dp)
                    )

                    Text(
                        text = "The app needs camera permission to scan QR codes",
                        textAlign = TextAlign.Center,
                        fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )

                    Button(
                        onClick = {
                            viewModel.provideOrRequestCameraPermission()
                        }
                    ) {
                        Text("Grant Camera Permission")
                    }
                }
            }
        }
    }
}