package com.cutiekyle.zhportremake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cutiekyle.zhportremake.ui.MainViewModel
import com.cutiekyle.zhportremake.ui.components.*
import com.cutiekyle.zhportremake.ui.theme.*

@Composable
fun PerformanceScreen(vm: MainViewModel) {
    val s by vm.settings.collectAsState()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // perf meters
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PerfMeter("FPS", "60", 1f, Modifier.weight(1f))
            PerfMeter("RAM GB", "1.2", 0.4f, Modifier.weight(1f), barColor = Orange)
            PerfMeter("GPU %", "67", 0.67f, Modifier.weight(1f))
            PerfMeter("°C", "38", 0.38f, Modifier.weight(1f))
        }
        ZHCard(title = "⚡ CPU") {
            ZHSlider("Threads", s.threadCount.toFloat(), 1f, 8f, "") { vm.saveSettings(s.copy(threadCount = it.toInt())) }
            Spacer(Modifier.height(8.dp))
            ZHDropdown("CPU Governor", s.cpuGov, listOf(
                "performance" to "Performance (max clocks)",
                "balanced"    to "Balanced",
                "powersave"   to "Powersave (battery)",
                "schedutil"   to "schedutil (kernel default)"
            )) { vm.saveSettings(s.copy(cpuGov = it)) }
        }
        ZHCard(title = "⚡ FRAME PACING") {
            ZHDropdown("FPS Cap", s.fpsCap.toString(), listOf("0" to "Uncapped", "30" to "30 FPS", "60" to "60 FPS", "90" to "90 FPS", "120" to "120 FPS"))
                { vm.saveSettings(s.copy(fpsCap = it.toIntOrNull() ?: 60)) }
            Spacer(Modifier.height(8.dp))
            ZHToggleRow("VSync",            "Eliminates tearing",                  s.vsync)      { vm.saveSettings(s.copy(vsync      = it)) }
            ZHToggleRow("Low-Latency Mode", "DXVK latency hint",                   s.lowLatency) { vm.saveSettings(s.copy(lowLatency = it)) }
        }
        ZHCard(title = "⚡ MEMORY") {
            ZHSlider("Heap Limit", s.heapLimit.toFloat(), 512f, 4096f, " MB") { vm.saveSettings(s.copy(heapLimit = it.toInt())) }
        }
    }
}

@Composable
fun PerfMeter(label: String, value: String, fraction: Float, modifier: Modifier = Modifier, barColor: androidx.compose.ui.graphics.Color = Gold) {
    Column(
        modifier = modifier.clip(RoundedCornerShape(5.dp)).background(Bg2).padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, color = Gold2, fontSize = 20.sp, fontFamily = FontFamily.Monospace)
        Text(label, color = TextDim, fontSize = 8.sp, fontFamily = FontFamily.Monospace, letterSpacing = 1.sp)
        Spacer(Modifier.height(5.dp))
        Box(Modifier.fillMaxWidth().height(3.dp).clip(RoundedCornerShape(2.dp)).background(Border)) {
            Box(Modifier.fillMaxWidth(fraction).fillMaxHeight().background(barColor))
        }
    }
}
