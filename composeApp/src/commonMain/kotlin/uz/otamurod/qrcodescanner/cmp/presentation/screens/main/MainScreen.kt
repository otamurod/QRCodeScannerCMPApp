package uz.otamurod.qrcodescanner.cmp.presentation.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.jetbrains.compose.resources.painterResource
import qrcodescannercmpapp.composeapp.generated.resources.Res
import qrcodescannercmpapp.composeapp.generated.resources.ic_generator
import qrcodescannercmpapp.composeapp.generated.resources.ic_scanner
import uz.otamurod.qrcodescanner.cmp.presentation.screens.qrgenerator.QRGeneratorScreen
import uz.otamurod.qrcodescanner.cmp.presentation.screens.qrscanner.QRScannerScreen

class MainScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        var selectedTab by remember { mutableStateOf(0) }

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),

            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = if (selectedTab == 0) {
                                "QR Code Scanner"
                            } else {
                                "QR Code Generator"
                            },
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    },

                    )
            },

            bottomBar = {
                NavigationBar {

                    NavigationBarItem(
                        label = {
                            Text(
                                text = "Scanner",
                            )
                        },
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(Res.drawable.ic_scanner),
                                contentDescription = "QR Code Scanner"
                            )
                        },
                    )

                    NavigationBarItem(
                        label = {
                            Text(
                                text = "Generator",
                            )
                        },
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(Res.drawable.ic_generator),
                                contentDescription = "QR Code Generator"
                            )
                        },
                    )
                }
            }
        ) {
            when (selectedTab) {
                0 -> QRScannerScreen()
                1 -> QRGeneratorScreen()
            }
        }
    }
}