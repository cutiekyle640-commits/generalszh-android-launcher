package com.cutiekyle.zhportremake.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Gold       = Color(0xFFC8A84B)
val Gold2      = Color(0xFFE2C46A)
val Gold3      = Color(0xFFF0D88A)
val GoldDim    = Color(0x1FC8A84B)
val GoldBorder = Color(0x40C8A84B)
val BgDeep     = Color(0xFF0A0C0F)
val Bg1        = Color(0xFF10141A)
val Bg2        = Color(0xFF161C24)
val Bg3        = Color(0xFF1C2430)
val Border     = Color(0xFF242E3C)
val Border2    = Color(0xFF2E3C4E)
val TextBright = Color(0xFFF0EBE0)
val TextMid    = Color(0xFFC8C4BC)
val TextDim    = Color(0xFF6A6560)
val Red        = Color(0xFFC84B4B)
val RedBg      = Color(0x1FC84B4B)
val Green      = Color(0xFF4BC87A)
val GreenBg    = Color(0x1F4BC87A)
val Blue       = Color(0xFF4B9AC8)
val BlueBg     = Color(0x1F4B9AC8)
val Orange     = Color(0xFFC8824B)
val OrangeBg   = Color(0x1FC8824B)

private val ZHColorScheme = darkColorScheme(
    primary          = Gold,
    onPrimary        = BgDeep,
    primaryContainer = GoldDim,
    secondary        = Gold2,
    onSecondary      = BgDeep,
    background       = BgDeep,
    onBackground     = TextMid,
    surface          = Bg1,
    onSurface        = TextMid,
    surfaceVariant   = Bg2,
    onSurfaceVariant = TextDim,
    outline          = Border,
    error            = Red,
    onError          = TextBright,
)

@Composable
fun ZHTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ZHColorScheme,
        typography  = Typography(),
        content     = content
    )
}
