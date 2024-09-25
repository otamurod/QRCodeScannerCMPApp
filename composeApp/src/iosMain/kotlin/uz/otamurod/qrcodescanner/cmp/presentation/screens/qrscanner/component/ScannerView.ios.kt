package uz.otamurod.qrcodescanner.cmp.presentation.screens.qrscanner.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.text.style.TextAlign
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVCaptureConnection
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceDiscoverySession.Companion.discoverySessionWithDeviceTypes
import platform.AVFoundation.AVCaptureDeviceInput.Companion.deviceInputWithDevice
import platform.AVFoundation.AVCaptureDevicePositionBack
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInDualCamera
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInDualWideCamera
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInDuoCamera
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInUltraWideCamera
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInWideAngleCamera
import platform.AVFoundation.AVCaptureMetadataOutput
import platform.AVFoundation.AVCaptureMetadataOutputObjectsDelegateProtocol
import platform.AVFoundation.AVCaptureOutput
import platform.AVFoundation.AVCapturePhotoOutput
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureSessionPresetPhoto
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.AVMetadataMachineReadableCodeObject
import platform.AVFoundation.AVMetadataObjectTypeMicroQRCode
import platform.AVFoundation.AVMetadataObjectTypeQRCode
import platform.AudioToolbox.AudioServicesPlaySystemSound
import platform.AudioToolbox.kSystemSoundID_Vibrate
import platform.UIKit.UIView
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue

private val deviceTypes = listOf(
    AVCaptureDeviceTypeBuiltInWideAngleCamera,
    AVCaptureDeviceTypeBuiltInDualWideCamera,
    AVCaptureDeviceTypeBuiltInDualCamera,
    AVCaptureDeviceTypeBuiltInUltraWideCamera,
    AVCaptureDeviceTypeBuiltInDuoCamera
)

@Composable
actual fun ScannerView(
    modifier: Modifier,
    onScanned: (String) -> Unit
) {
    val camera = remember {
        discoverySessionWithDeviceTypes(
            deviceTypes = deviceTypes,
            mediaType = AVMediaTypeVideo,
            position = AVCaptureDevicePositionBack,
        ).devices.firstOrNull() as? AVCaptureDevice
    }

    if (camera != null) {
        CameraView(
            camera = camera,
            onScanned = onScanned
        )
    } else {
        Text(
            text = "Camera is not available on simulator.",
            color = Color.Blue,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding()
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
private fun CameraView(
    camera: AVCaptureDevice,
    onScanned: (String) -> Unit,
) {
    val capturePhotoOutput = remember { AVCapturePhotoOutput() }

    val metadataOutput = remember { AVCaptureMetadataOutput() }

    val captureSession = remember {
        AVCaptureSession().also { captureSession ->
            captureSession.sessionPreset = AVCaptureSessionPresetPhoto

            val captureDeviceInput = deviceInputWithDevice(
                device = camera,
                error = null
            )

            if (captureDeviceInput != null && captureSession.canAddInput(captureDeviceInput)) {
                captureSession.addInput(captureDeviceInput)
            }

            if (captureSession.canAddOutput(capturePhotoOutput)) {
                captureSession.addOutput(capturePhotoOutput)
            }

            if (captureSession.canAddOutput(metadataOutput)) {
                captureSession.addOutput(metadataOutput)

                metadataOutput.setMetadataObjectsDelegate(
                    objectsDelegate = object : NSObject(),
                        AVCaptureMetadataOutputObjectsDelegateProtocol {

                        override fun captureOutput(
                            output: AVCaptureOutput,
                            didOutputMetadataObjects: List<*>,
                            fromConnection: AVCaptureConnection,
                        ) {
                            didOutputMetadataObjects.firstOrNull()?.let { metadataObject ->
                                val readableObject =
                                    metadataObject as? AVMetadataMachineReadableCodeObject

                                val scannedCode = readableObject?.stringValue

                                if (!scannedCode.isNullOrEmpty()) {
                                    AudioServicesPlaySystemSound(kSystemSoundID_Vibrate)

                                    captureSession.stopRunning()

                                    onScanned(scannedCode)
                                }
                            }
                        }
                    },

                    queue = dispatch_get_main_queue()
                )

                metadataOutput.metadataObjectTypes = listOf(
                    AVMetadataObjectTypeQRCode,
                    AVMetadataObjectTypeMicroQRCode,
                )
            }
        }
    }

    val cameraPreviewLayer = remember {
        AVCaptureVideoPreviewLayer(session = captureSession)
    }

    DisposableEffect(Unit) {
        onDispose {
            captureSession.stopRunning()
        }
    }

    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            val cameraContainer = UIView()

            cameraContainer.layer.addSublayer(cameraPreviewLayer)
            cameraPreviewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill

            captureSession.startRunning()

            cameraContainer
        }
    )
}