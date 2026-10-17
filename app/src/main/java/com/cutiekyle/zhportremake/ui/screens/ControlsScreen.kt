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
fun ControlsScreen(vm: MainViewModel) {
    val s by vm.settings.collectAsState()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ZHCard(title = "✦ TOUCH GESTURES") {
            ZHToggleRow("Tap to Select",           "Single tap selects unit",                  true) {}
            ZHToggleRow("Long-Press Box Select",   "Hold + drag = selection box",              true) {}
            ZHToggleRow("Two-Finger Pan",          "Two fingers = camera scroll",              true) {}
            ZHToggleRow("Pinch Zoom",              "Classic pinch in/out",                     true) {}
            ZHToggleRow("Double-Tap Select Type",  "Select all same type on screen",           true) {}
            ZHToggleRow("Camera Inertia",          "Pan momentum on release",                  true) {}
            ZHToggleRow("Edge Scroll",             "Drag to edge to scroll camera",            false) {}
        }
        ZHCard(title = "✦ SENSITIVITY") {
            ZHSlider("Pan Speed",         s.panSpeed.toFloat(),  1f, 10f, "") { vm.saveSettings(s.copy(panSpeed  = it.toInt())) }
            Spacer(Modifier.height(8.dp))
            ZHSlider("Zoom Speed",        s.zoomSpeed.toFloat(), 1f, 10f, "") { vm.saveSettings(s.copy(zoomSpeed = it.toInt())) }
            Spacer(Modifier.height(8.dp))
            ZHSlider("Long Press Delay",  400f, 150f, 800f, " ms") {}
            Spacer(Modifier.height(8.dp))
            ZHSlider("Dead Zone",         8f,   2f,   24f,  " px") {}
        }
        ZHCard(title = "✦ HUD") {
            ZHSlider("HUD Opacity",  s.hudOpacity.toFloat(), 20f, 100f, "%") { vm.saveSettings(s.copy(hudOpacity = it.toInt())) }
        }
    }
}
