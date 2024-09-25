package uz.otamurod.qrcodescanner.cmp.presentation.screens.qrgenerator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uz.otamurod.qrcodescanner.cmp.presentation.screens.qrgenerator.component.QRCodeView
import uz.otamurod.qrcodescanner.cmp.ui.theme.md_theme_light_onPrimary

@Composable
fun QRGeneratorScreen(
    modifier: Modifier = Modifier,
) {
    var input by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = {
                input = it
                isGenerating = false
            },
            label = { Text("Enter text or URL") },
            maxLines = 5,
            modifier = Modifier
                .padding(top = 76.dp)
                .fillMaxWidth()
        )

        Button(
            onClick = {
                isGenerating = true
            }
        ) {
            Text("Generate QR Code")
        }

        AnimatedVisibility(input.isEmpty()) {
            Text("Please enter text or URL")
        }

        AnimatedVisibility(isGenerating && input.isNotEmpty()) {
            QRCodeView(
                value = input,
                modifier = Modifier.background(
                    color = md_theme_light_onPrimary
                )
            )
        }
    }
}