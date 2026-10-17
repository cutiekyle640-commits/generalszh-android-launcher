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
fun NetworkScreen(vm: MainViewModel) {
    val s by vm.settings.collectAsState()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ZHCard(title = "⌘ GENERALS ONLINE") {
            ZHTextField("Server", s.goServer, "https://api.generalsonline.net") { vm.saveSettings(s.copy(goServer = it)) }
            Spacer(Modifier.height(8.dp))
            ZHToggleRow("Auto-Login", "Resume session on launch", true) {}
        }
        ZHCard(title = "⌘ P2P / STUN") {
            ZHTextField("STUN Server", s.stunServer, "stun:stun.generalsonline.net:3478") { vm.saveSettings(s.copy(stunServer = it)) }
            Spacer(Modifier.height(8.dp))
            ZHToggleRow("ICE/STUN",          "NAT traversal for P2P matches",      true)  {}
            ZHToggleRow("Force TURN Relay",  "Guaranteed connection via relay",    false) {}
            ZHToggleRow("Multicast Lock",    "Required for LAN lobbies on Android",true)  {}
            ZHToggleRow("LAN Wi-Fi Only",    "Listen on Wi-Fi, not mobile data",   true)  {}
        }
        ZHCard(title = "⌘ BANDWIDTH") {
            ZHSlider("Max Upload", 512f, 64f, 2048f, " kbps") {}
            Spacer(Modifier.height(8.dp))
            ZHToggleRow("Low-Bandwidth Mode", "Reduce sync on poor connections", false) {}
        }
    }
}
