package uz.otamurod.qrcodescanner.cmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform