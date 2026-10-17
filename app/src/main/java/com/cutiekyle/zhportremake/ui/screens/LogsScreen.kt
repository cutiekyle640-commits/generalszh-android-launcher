package com.cutiekyle.zhportremake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cutiekyle.zhportremake.ui.LogEntry
import com.cutiekyle.zhportremake.ui.MainViewModel
import com.cutiekyle.zhportremake.ui.components.ZHCard
import com.cutiekyle.zhportremake.ui.theme.*

@Composable
fun LogsScreen(vm: MainViewModel) {
    val logs by vm.logs.collectAsState()
    var filter by remember { mutableStateOf("all") }
    val filtered = when (filter) {
        "info" -> logs.filter { it.level in listOf("info", "warn", "err", "ok") }
        "warn" -> logs.filter { it.level in listOf("warn", "err") }
        "err"  -> logs.filter { it.level == "err" }
        else   -> logs
    }
    val listState = rememberLazyListState()

    Column(Modifier.fillMaxSize().padding(14.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 10.dp)) {
            OutlinedButton(onClick = { vm.clearLogs() }, modifier = Modifier.height(34.dp)) {
                Text("CLEAR", fontFamily = FontFamily.Monospace, fontSize = 9.sp, color = TextMid)
            }
            listOf("all", "info", "warn", "err").forEach { level ->
                FilterChip(
                    selected = filter == level,
                    onClick  = { filter = level },
                    label    = { Text(level.uppercase(), fontFamily = FontFamily.Monospace, fontSize = 8.sp) },
                    colors   = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GoldDim,
                        selectedLabelColor     = Gold
                    )
                )
            }
        }
        ZHCard(title = "▤ LOG — ${filtered.size} lines") {
            LazyColumn(
                state  = listState,
                modifier = Modifier.fillMaxWidth().height(400.dp),
                reverseLayout = false
            ) {
                items(filtered) { entry ->
                    LogLine(entry)
                }
            }
        }
    }
}

@Composable
fun LogLine(entry: LogEntry) {
    val color = when (entry.level) {
        "err"  -> Red
        "warn" -> Gold
        "ok"   -> Green
        "info" -> Blue
        else   -> TextDim
    }
    Text(
        "[${entry.ts}] ${entry.msg}",
        color      = color,
        fontSize   = 10.sp,
        fontFamily = FontFamily.Monospace,
        lineHeight = 16.sp,
        modifier   = Modifier.padding(vertical = 1.dp)
    )
}
