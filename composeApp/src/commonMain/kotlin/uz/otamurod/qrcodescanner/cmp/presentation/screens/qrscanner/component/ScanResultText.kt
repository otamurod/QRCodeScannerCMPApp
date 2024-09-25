package uz.otamurod.qrcodescanner.cmp.presentation.screens.qrscanner.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun ScanResultText(
    scanResult: String,
    onLinkClick: (String) -> Unit,
    scanResultColor: Color,
    modifier: Modifier = Modifier
) {
    if (scanResult.isValidUrl()) {
        val annotatedString = buildAnnotatedString {
            append("Scan Result: ")

            pushStringAnnotation(
                tag = "URL",
                annotation = scanResult
            )
            withStyle(
                style = SpanStyle(
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(scanResult)
            }
            pop()
        }

        ClickableText(
            text = annotatedString,
            modifier = modifier
                .padding(end = 8.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                color = scanResultColor,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            onClick = { offset ->
                annotatedString.getStringAnnotations(
                    tag = "URL",
                    start = offset,
                    end = offset
                ).firstOrNull()?.let { annotation ->
                    onLinkClick(annotation.item)
                }
            }
        )
    } else {
        Text(
            text = "Scan Result: \"$scanResult\"",
            modifier = modifier
                .padding(end = 8.dp),
            color = scanResultColor,
            fontStyle = MaterialTheme.typography.labelMedium.fontStyle,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun String.isValidUrl(): Boolean {
    val regex = "([A-Za-z]*:\\/\\/)?\\S*".toRegex()

    return this.startsWith("http://") || this.startsWith("https://") && regex.containsMatchIn(this)
}
