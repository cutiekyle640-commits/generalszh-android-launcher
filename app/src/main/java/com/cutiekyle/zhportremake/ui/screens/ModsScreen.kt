package com.cutiekyle.zhportremake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import java.util.UUID

@Composable
fun ModsScreen(vm: MainViewModel) {
    val settings by vm.settings.collectAsState()
    val mods = settings.mods
    val compat = checkCompat(mods)
    var showAddMod by remember { mutableStateOf(false) }
    var editMod by remember { mutableStateOf<ModInfo?>(null) }

    Column(Modifier.fillMaxSize()) {
        // action bar
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick  = { editMod = null; showAddMod = true },
                colors   = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = BgDeep),
                modifier = Modifier.height(36.dp)
            ) { Text("+ ADD MOD", fontFamily = FontFamily.Monospace, fontSize = 10.sp, letterSpacing = 1.sp) }
            OutlinedButton(
                onClick = { /* import */ },
                border  = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                colors  = ButtonDefaults.outlinedButtonColors(contentColor = TextMid),
                modifier = Modifier.height(36.dp)
            ) { Text("↑ IMPORT", fontFamily = FontFamily.Monospace, fontSize = 10.sp) }
            OutlinedButton(
                onClick  = { /* export */ },
                border   = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                colors   = ButtonDefaults.outlinedButtonColors(contentColor = TextMid),
                modifier = Modifier.height(36.dp)
            ) { Text("↓ EXPORT", fontFamily = FontFamily.Monospace, fontSize = 10.sp) }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                ZHCard(
                    title  = "◈ INSTALLED MODS",
                    action = { Text("${mods.count { it.enabled }}/${mods.size} active", color = TextDim, fontSize = 9.sp, fontFamily = FontFamily.Monospace) }
                ) {
                    if (mods.isEmpty()) {
                        EmptyState("No mods registered", "Tap + ADD MOD to start")
                    } else {
                        val conflictNames = compat.conflicts.flatMap { listOf(it.first, it.second) }.toSet()
                        val missingMods   = compat.missingReqs.map { it.first }.toSet()
                        mods.sortedBy { it.priority }.forEachIndexed { i, mod ->
                            val hasConflict = mod.name in conflictNames
                            val hasMissing  = mod.name in missingMods
                            ModRow(
                                mod         = mod,
                                hasConflict = hasConflict,
                                hasMissing  = hasMissing,
                                onToggle    = { vm.updateMods(mods.map { if (it.id == mod.id) it.copy(enabled = !it.enabled) else it }) },
                                onEdit      = { editMod = mod; showAddMod = true },
                                onDelete    = { vm.updateMods(mods.filter { it.id != mod.id }) }
                            )
                            if (i < mods.size - 1) Divider(color = Border, thickness = 1.dp)
                        }
                    }
                }
            }

            item {
                // compat matrix
                ZHCard(title = "⬡ COMPATIBILITY MATRIX") {
                    if (mods.size < 2) {
                        EmptyState("Add 2+ mods", "to see the compatibility matrix")
                    } else {
                        Column {
                            Row {
                                Spacer(Modifier.width(100.dp))
                                mods.forEach { m ->
                                    Text(
                                        m.name.take(9) + if (m.name.length > 9) "…" else "",
                                        color = Gold, fontSize = 8.sp, fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.width(56.dp)
                                    )
                                }
                            }
                            mods.forEach { row ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        row.name.take(12) + if (row.name.length > 12) "…" else "",
                                        color = TextMid, fontSize = 9.sp, fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.width(100.dp)
                                    )
                                    mods.forEach { col ->
                                        if (row.id == col.id) {
                                            Box(Modifier.width(56.dp).padding(2.dp).clip(RoundedCornerShape(2.dp)).background(Bg3).padding(horizontal = 4.dp, vertical = 2.dp)) {
                                                Text("■", color = TextDim, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                            }
                                        } else {
                                            val conflict = row.conflicts.any { it.equals(col.name, true) } || col.conflicts.any { it.equals(row.name, true) }
                                            val req      = row.requires.any { it.equals(col.name, true) }
                                            val bg    = if (conflict) RedBg else if (req) GreenBg else Bg2
                                            val fg    = if (conflict) Red  else if (req) Green   else TextDim
                                            val sym   = if (conflict) "✕" else if (req) "↑" else "✓"
                                            Box(Modifier.width(56.dp).padding(2.dp).clip(RoundedCornerShape(2.dp)).background(bg).padding(horizontal = 4.dp, vertical = 2.dp)) {
                                                Text(sym, color = fg, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                                Text("↑ dep",      color = Green,   fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                Text("✕ conflict", color = Red,     fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                Text("✓ ok",       color = TextDim, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(14.dp)) }
        }
    }

    if (showAddMod) {
        ModDialog(
            initial  = editMod,
            nextPrio = mods.size,
            onDismiss = { showAddMod = false; editMod = null },
            onSave   = { mod ->
                val updated = if (mods.any { it.id == mod.id })
                    mods.map { if (it.id == mod.id) mod else it }
                else mods + mod
                vm.updateMods(updated)
                showAddMod = false; editMod = null
            }
        )
    }
}

@Composable
fun ModRow(mod: ModInfo, hasConflict: Boolean, hasMissing: Boolean, onToggle: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(mod.name, color = TextBright, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                Text(
                    "${mod.author.ifBlank { mod.folderPath }}${if (mod.version.isNotBlank()) " · v${mod.version}" else ""} · #${mod.priority}",
                    color = TextDim, fontSize = 10.sp, fontFamily = FontFamily.Monospace
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(top = 4.dp)) {
                    ZHTag(if (mod.enabled) "ON" else "OFF", tagColorGold = mod.enabled, tagColorGray = !mod.enabled)
                    if (hasConflict)  ZHTag("CONFLICT",    tagColorRed    = true)
                    if (hasMissing)   ZHTag("MISSING REQ", tagColorRed    = true)
                    if (mod.requires.isNotEmpty() && !hasMissing) ZHTag("DEPS OK", tagColorGreen = true)
                    if (mod.conflicts.isNotEmpty()) ZHTag("CONFLICTS", tagColorOrange = true)
                }
            }
            Switch(
                checked  = mod.enabled,
                onCheckedChange = { onToggle() },
                colors   = SwitchDefaults.colors(checkedThumbColor = BgDeep, checkedTrackColor = Gold, uncheckedTrackColor = Border2)
            )
        }
        if (expanded) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp).padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = onEdit)   { Text("EDIT",   color = Gold,  fontSize = 9.sp, fontFamily = FontFamily.Monospace) }
                TextButton(onClick = onDelete) { Text("DELETE", color = Red,   fontSize = 9.sp, fontFamily = FontFamily.Monospace) }
            }
        }
        TextButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth().height(24.dp)
        ) {
            Text(if (expanded) "▲ LESS" else "▼ MORE", color = TextDim, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
        }
    }
}

@Composable
fun ModDialog(initial: ModInfo?, nextPrio: Int, onDismiss: () -> Unit, onSave: (ModInfo) -> Unit) {
    var name     by remember { mutableStateOf(initial?.name ?: "") }
    var author   by remember { mutableStateOf(initial?.author ?: "") }
    var version  by remember { mutableStateOf(initial?.version ?: "") }
    var folder   by remember { mutableStateOf(initial?.folderPath ?: "") }
    var priority by remember { mutableStateOf((initial?.priority ?: nextPrio).toString()) }
    var desc     by remember { mutableStateOf(initial?.description ?: "") }
    var notes    by remember { mutableStateOf(initial?.notes ?: "") }
    var exe      by remember { mutableStateOf(initial?.exeOverride ?: "") }
    var requires by remember { mutableStateOf(initial?.requires?.joinToString(", ") ?: "") }
    var conflicts by remember { mutableStateOf(initial?.conflicts?.joinToString(", ") ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor   = Bg1,
        title = { Text(if (initial == null) "+ ADD MOD" else "✎ EDIT MOD", color = Gold2, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ZHTextField("Mod Name *", name, "Zero Hour Reborn") { name = it }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(Modifier.weight(1f)) { ZHTextField("Author", author, "Team") { author = it } }
                    Box(Modifier.weight(1f)) { ZHTextField("Version", version, "2.1.4") { version = it } }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(Modifier.weight(1f)) { ZHTextField("Priority", priority, "0") { priority = it } }
                    Box(Modifier.weight(1f)) { ZHTextField("EXE Override", exe, "generals.exe") { exe = it } }
                }
                ZHTextField("Folder Path *", folder, "Mods/ZHReborn") { folder = it }
                ZHTextField("Description", desc, "What this mod does…") { desc = it }
                ZHTextField("Requires (comma separated)", requires, "BasePatch, Other") { requires = it }
                ZHTextField("Conflicts (comma separated)", conflicts, "ShockWave") { conflicts = it }
                ZHTextField("Notes", notes, "Requires ZH 1.04") { notes = it }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && folder.isNotBlank()) {
                        onSave(ModInfo(
                            id          = initial?.id ?: UUID.randomUUID().toString(),
                            name        = name.trim(),
                            author      = author.trim(),
                            version     = version.trim(),
                            folderPath  = folder.trim(),
                            exeOverride = exe.trim(),
                            priority    = priority.trim().toIntOrNull() ?: nextPrio,
                            enabled     = initial?.enabled ?: true,
                            description = desc.trim(),
                            notes       = notes.trim(),
                            requires    = requires.split(",").map { it.trim() }.filter { it.isNotBlank() },
                            conflicts   = conflicts.split(",").map { it.trim() }.filter { it.isNotBlank() }
                        ))
                    }
                },
                enabled = name.isNotBlank() && folder.isNotBlank()
            ) { Text("SAVE", color = Gold, fontFamily = FontFamily.Monospace) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("CANCEL", color = TextDim, fontFamily = FontFamily.Monospace) } }
    )
}
