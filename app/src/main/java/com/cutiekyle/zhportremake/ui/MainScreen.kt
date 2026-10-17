package com.cutiekyle.zhportremake.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cutiekyle.zhportremake.ui.screens.*
import com.cutiekyle.zhportremake.ui.theme.*

data class NavTab(val label: String, val icon: ImageVector)

val TABS = listOf(
    NavTab("Launch",      Icons.Default.PlayArrow),
    NavTab("Mods",        Icons.Default.Extension),
    NavTab("Graphics",    Icons.Default.Visibility),
    NavTab("Performance", Icons.Default.Speed),
    NavTab("Controls",    Icons.Default.TouchApp),
    NavTab("Network",     Icons.Default.Wifi),
    NavTab("Profiles",    Icons.Default.AccountCircle),
    NavTab("Logs",        Icons.Default.Article),
)

@Composable
fun MainScreen(vm: MainViewModel) {
    val settings by vm.settings.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep)
            .systemBarsPadding()
    ) {
        // ── top bar ──
        Surface(
            color = Bg1,
            tonalElevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "⬡ ZH PORT REMAKE",
                            color = Gold2,
                            fontSize = 15.sp,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 3.sp
                        )
                        Text(
                            "by cutiekyle._. · v1.0.0",
                            color = TextDim,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 2.sp
                        )
                    }
                    val compat = checkCompat(settings.mods)
                    Surface(
                        shape = MaterialTheme.shapes.extraLarge,
                        color = Bg2
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .background(
                                        if (!compat.isClean) Gold else Green,
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    )
                            )
                            Text(
                                if (!compat.isClean) "ISSUES" else if (settings.mods.any { it.enabled }) "READY" else "NO MODS",
                                color = TextDim,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
                // scrollable tab row
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor   = Bg1,
                    contentColor     = Gold,
                    edgePadding      = 12.dp,
                    indicator        = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Gold
                        )
                    },
                    divider = { Divider(color = Border, thickness = 1.dp) }
                ) {
                    TABS.forEachIndexed { i, tab ->
                        Tab(
                            selected  = selectedTab == i,
                            onClick   = { selectedTab = i },
                            text = {
                                Text(
                                    tab.label,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize   = 10.sp,
                                    letterSpacing = 1.sp
                                )
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label, modifier = Modifier.size(16.dp)) },
                            selectedContentColor   = Gold2,
                            unselectedContentColor = TextDim
                        )
                    }
                }
            }
        }

        // ── content ──
        when (selectedTab) {
            0 -> LaunchScreen(vm, onGoToMods = { selectedTab = 1 })
            1 -> ModsScreen(vm)
            2 -> GraphicsScreen(vm)
            3 -> PerformanceScreen(vm)
            4 -> ControlsScreen(vm)
            5 -> NetworkScreen(vm)
            6 -> ProfilesScreen(vm)
            7 -> LogsScreen(vm)
        }
    }
}
