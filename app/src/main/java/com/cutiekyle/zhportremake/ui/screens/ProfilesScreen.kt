package com.cutiekyle.zhportremake.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cutiekyle.zhportremake.data.checkCompat
import com.cutiekyle.zhportremake.ui.MainViewModel
import com.cutiekyle.zhportremake.ui.components.*
import com.cutiekyle.zhportremake.ui.theme.*

@Composable
fun ProfilesScreen(vm: MainViewModel) {
    val s by vm.settings.collectAsState()
    val compat = checkCompat(s.mods)
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ZHCard(title = "☰ CURRENT CONFIG") {
            InfoRow("Active Mods", s.mods.filter { it.enabled }.map { it.name }.joinToString(", ").ifBlank { "None" })
            InfoRow("Renderer",   s.renderer.uppercase())
            InfoRow("FPS Cap",    if (s.fpsCap == 0) "Uncapped" else "${s.fpsCap} fps")
            InfoRow("Resolution", s.resolution)
            InfoRow("Compat",     if (compat.isClean) "Clean ✓" else "${compat.conflicts.size} conflict(s)")
        }
        ZHCard(title = "☰ QUICK PROFILES") {
            listOf(
                Triple("⚔", "Vanilla ZH",      "No mods · Vulkan · 60fps"),
                Triple("🛡", "ShockWave HD",     "ShockWave · High · 60fps"),
                Triple("⚡", "Performance Mode", "No mods · OpenGL ES · 30fps")
            ).forEach { (icon, name, sub) ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(icon, fontSize = 20.sp)
                        Column {
                            Text(name, color = TextBright, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                            Text(sub,  color = TextDim,    fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                    TextButton(onClick = { vm.log("Profile '$name' applied", "ok") }) {
                        Text("APPLY", color = Gold, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    }
                }
                Divider(color = Border, thickness = 1.dp)
            }
        }
    }
}
