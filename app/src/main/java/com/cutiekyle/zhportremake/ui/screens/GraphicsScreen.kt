package com.cutiekyle.zhportremake.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cutiekyle.zhportremake.ui.MainViewModel
import com.cutiekyle.zhportremake.ui.components.*

@Composable
fun GraphicsScreen(vm: MainViewModel) {
    val s by vm.settings.collectAsState()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ZHCard(title = "◻ RENDERER") {
            ZHDropdown("Backend", s.renderer, listOf(
                "vulkan"    to "Vulkan (DXVK) — Adreno 7xx/8xx",
                "opengles3" to "OpenGL ES 3.1 — broad support",
                "opengles2" to "OpenGL ES 2.0 — Mali fallback",
                "turnip"    to "Turnip — advanced"
            )) { vm.saveSettings(s.copy(renderer = it)) }
        }
        ZHCard(title = "◻ RESOLUTION") {
            ZHDropdown("Resolution", s.resolution, listOf(
                "native" to "Native (device)",
                "1080p"  to "1920×1080",
                "1440p"  to "2560×1440",
                "720p"   to "1280×720"
            )) { vm.saveSettings(s.copy(resolution = it)) }
        }
        ZHCard(title = "◻ QUALITY") {
            ZHDropdown("Texture Quality", s.texQuality, listOf("high" to "High", "medium" to "Medium", "low" to "Low"))
                { vm.saveSettings(s.copy(texQuality = it)) }
            Spacer(Modifier.height(8.dp))
            ZHDropdown("Shadow Detail", s.shadow, listOf("off" to "Off", "low" to "Low", "medium" to "Medium", "high" to "High"))
                { vm.saveSettings(s.copy(shadow = it)) }
            Spacer(Modifier.height(8.dp))
            ZHToggleRow("Particles",       "Explosions, dust, smoke",              s.particles)   { vm.saveSettings(s.copy(particles   = it)) }
            ZHToggleRow("Dynamic Lighting","Disable on low-end devices",           s.dynLighting) { vm.saveSettings(s.copy(dynLighting = it)) }
            ZHToggleRow("MSAA",            "Anti-aliasing — heavy on mobile",      s.msaa)        { vm.saveSettings(s.copy(msaa        = it)) }
        }
        ZHCard(title = "◻ UI SCALE") {
            ZHSlider("UI Scale", s.uiScale.toFloat(), 75f, 200f, "%") { vm.saveSettings(s.copy(uiScale = it.toInt())) }
        }
    }
}
