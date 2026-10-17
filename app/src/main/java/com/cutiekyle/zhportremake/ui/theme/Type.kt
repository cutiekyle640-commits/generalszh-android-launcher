package com.cutiekyle.zhportremake.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    bodyLarge  = TextStyle(fontFamily=FontFamily.Monospace, fontWeight=FontWeight.Normal, fontSize=13.sp, lineHeight=20.sp),
    bodyMedium = TextStyle(fontFamily=FontFamily.Monospace, fontWeight=FontWeight.Normal, fontSize=11.sp, lineHeight=17.sp),
    bodySmall  = TextStyle(fontFamily=FontFamily.Monospace, fontWeight=FontWeight.Normal, fontSize=9.sp,  lineHeight=14.sp),
    labelLarge = TextStyle(fontFamily=FontFamily.Monospace, fontWeight=FontWeight.Bold,   fontSize=10.sp, letterSpacing=1.5.sp),
    labelSmall = TextStyle(fontFamily=FontFamily.Monospace, fontWeight=FontWeight.Normal, fontSize=9.sp,  letterSpacing=1.sp),
    titleMedium= TextStyle(fontFamily=FontFamily.Monospace, fontWeight=FontWeight.Bold,   fontSize=13.sp, letterSpacing=2.sp),
    titleLarge = TextStyle(fontFamily=FontFamily.Monospace, fontWeight=FontWeight.Bold,   fontSize=16.sp, letterSpacing=3.sp),
)
