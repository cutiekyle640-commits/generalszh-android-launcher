package com.cutiekyle.zhportremake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cutiekyle.zhportremake.data.*
import com.cutiekyle.zhportremake.ui.MainViewModel
import com.cutiekyle.zhportremake.ui.components.*
import com.cutiekyle.zhportremake.ui.theme.*

@Composable
fun LaunchScreen(vm: MainViewModel, onGoToMods: () -> Unit) {
    val settings by vm.settings.collectAsState()
    val compat = checkCompat(settings.mods)
    val loadOrder = topoSort(settings.mods)
    val cmd = vm.buildLaunchCmd()
    var showSettings by remember { mutableStateOf(false) }
    var copied by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // issues
        if (!compat.isClean) {
            ZHCard(title = "⚠ ISSUES") {
                compat.conflicts.forEach { (a, b) ->
                    AlertRow(text = "CONFLICT — $a ↔ $b", isError = true)
                }
                compat.missingReqs.forEach { (m, r) ->
                    AlertRow(text = "MISSING REQ — $m needs $r", isError = true)
                }
            }
        }

        // launch control
        ZHCard(title = "▶ LAUNCH CONTROL") {
            if (cmd.isNotBlank()) {
                Text(
                    "LAUNCH COMMAND — tap to copy",
                    color = TextDim, fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace, letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(BgDeep)
                        .border(1.dp, if (copied) Green else GreenBg, RoundedCornerShape(4.dp))
                        .clickable { vm.copyLaunchCmd(); copied = true }
                        .padding(10.dp)
                ) {
                    Text(cmd, color = Green, fontSize = 10.sp, fontFamily = FontFamily.Monospace, lineHeight = 16.sp)
                }
                Spacer(Modifier.height(10.dp))
            } else {
                Text(
                    "Enable at least one mod to generate a launch command.",
                    color = TextDim, fontSize = 11.sp, fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }
            Button(
                onClick = { vm.copyLaunchCmd(); copied = true },
                enabled = cmd.isNotBlank() && compat.isClean,
                colors  = ButtonDefaults.buttonColors(containerColor = GreenBg, contentColor = Green),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    if (copied) "✓ COPIED" else "COPY LAUNCH COMMAND",
                    fontFamily = FontFamily.Monospace, letterSpacing = 1.sp
                )
            }
            LaunchedEffect(copied) {
                if (copied) { kotlinx.coroutines.delay(2500); copied = false }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                "Paste into a terminal or .bat file on your Windows/Android device",
                color = TextDim, fontSize = 9.sp, fontFamily = FontFamily.Monospace
            )
        }

        // game dir
        ZHCard(
            title  = "⚙ GAME DIRECTORY",
            action = { TextButton(onClick = { showSettings = true }) { Text("EDIT", color = Gold, fontSize = 9.sp, fontFamily = FontFamily.Monospace) } }
        ) {
            InfoRow("Dir",      settings.gameDir.ifBlank { "Not set" })
            InfoRow("EXE",      settings.gameExe.ifBlank { "generals.exe" })
            InfoRow("Renderer", settings.renderer.uppercase())
        }

        // load order
        ZHCard(title = "◈ LOAD ORDER") {
            if (loadOrder.isNullOrEmpty()) {
                EmptyState("No mods active", "Enable mods in the Mods tab", onGoToMods)
            } else {
                loadOrder.forEachIndexed { i, mod ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(Bg2)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${i + 1}", color = Gold, fontSize = 11.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.width(24.dp))
                        Column(Modifier.weight(1f)) {
                            Text(mod.name, color = TextBright, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                            Text(mod.folderPath, color = TextDim, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                        }
                        if (mod.requires.isNotEmpty()) ZHTag("DEPS ✓", tagColorGreen = true)
                    }
                    if (i < loadOrder.size - 1) Spacer(Modifier.height(5.dp))
                }
            }
        }
    }

    if (showSettings) SettingsDialog(settings = settings, onDismiss = { showSettings = false }, onSave = { vm.saveSettings(it) })
}

@Composable
fun SettingsDialog(settings: com.cutiekyle.zhportremake.data.AppSettings, onDismiss: () -> Unit, onSave: (com.cutiekyle.zhportremake.data.AppSettings) -> Unit) {
    var dir by remember { mutableStateOf(settings.gameDir) }
    var exe by remember { mutableStateOf(settings.gameExe) }
    var renderer by remember { mutableStateOf(settings.renderer) }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor   = Bg1,
        title = { Text("⚙ SETTINGS", color = Gold2, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ZHTextField("Game Directory", dir, "C:\\ZH") { dir = it }
                ZHTextField("Game EXE", exe, "generals.exe") { exe = it }
                ZHDropdown(
                    label = "Renderer",
                    selected = renderer,
                    options  = listOf("vulkan" to "Vulkan (DXVK)", "opengles3" to "OpenGL ES 3.1", "opengles2" to "OpenGL ES 2.0", "turnip" to "Turnip")
                ) { renderer = it }
            }
        },
        confirmButton = { TextButton(onClick = { onSave(settings.copy(gameDir = dir, gameExe = exe, renderer = renderer)); onDismiss() }) { Text("SAVE", color = Gold, fontFamily = FontFamily.Monospace) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("CANCEL", color = TextDim, fontFamily = FontFamily.Monospace) } }
    )
}
