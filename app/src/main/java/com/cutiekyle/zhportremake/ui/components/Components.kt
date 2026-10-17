package com.cutiekyle.zhportremake.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cutiekyle.zhportremake.ui.theme.*

// ── ZH Card ──────────────────────────────────────────────────────────────────
@Composable
fun ZHCard(title: String, action: (@Composable () -> Unit)? = null, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Bg1)
            .border(1.dp, Border, RoundedCornerShape(6.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Bg2).padding(horizontal = 14.dp, vertical = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(title, color = Gold, fontSize = 10.sp, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)
            action?.invoke()
        }
        Divider(color = Border, thickness = 1.dp)
        Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) { content() }
    }
}

// ── Toggle Row ───────────────────────────────────────────────────────────────
@Composable
fun ZHToggleRow(label: String, sub: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f).padding(end = 12.dp)) {
            Text(label, color = TextBright, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
            Text(sub,   color = TextDim,    fontSize = 10.sp, fontFamily = FontFamily.Monospace, lineHeight = 14.sp)
        }
        Switch(
            checked         = checked,
            onCheckedChange = onChange,
            colors          = SwitchDefaults.colors(checkedThumbColor = BgDeep, checkedTrackColor = Gold, uncheckedTrackColor = Border2)
        )
    }
    Divider(color = Border, thickness = 1.dp)
}

// ── Slider ───────────────────────────────────────────────────────────────────
@Composable
fun ZHSlider(label: String, value: Float, min: Float, max: Float, unit: String, onChange: (Float) -> Unit) {
    var v by remember(value) { mutableFloatStateOf(value) }
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = TextDim, fontSize = 9.sp, fontFamily = FontFamily.Monospace, letterSpacing = 1.sp)
            Text("${v.toInt()}$unit", color = Gold, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
        }
        Slider(
            value         = v,
            onValueChange = { v = it },
            onValueChangeFinished = { onChange(v) },
            valueRange    = min..max,
            colors        = SliderDefaults.colors(thumbColor = Gold, activeTrackColor = Gold, inactiveTrackColor = Border2),
            modifier      = Modifier.fillMaxWidth()
        )
    }
}

// ── Dropdown ─────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZHDropdown(label: String, selected: String, options: List<Pair<String, String>>, onChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = options.firstOrNull { it.first == selected }?.second ?: selected
    Column {
        Text(label, color = TextDim, fontSize = 9.sp, fontFamily = FontFamily.Monospace, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 4.dp))
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value         = selectedLabel,
                onValueChange = {},
                readOnly      = true,
                trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                colors        = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = Gold,
                    unfocusedBorderColor = Border,
                    focusedTextColor     = TextBright,
                    unfocusedTextColor   = TextBright,
                    focusedContainerColor   = BgDeep,
                    unfocusedContainerColor = BgDeep
                ),
                textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                modifier  = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded        = expanded,
                onDismissRequest = { expanded = false },
                modifier        = Modifier.background(Bg2)
            ) {
                options.forEach { (value, display) ->
                    DropdownMenuItem(
                        text    = { Text(display, fontFamily = FontFamily.Monospace, fontSize = 12.sp, color = if (value == selected) Gold else TextMid) },
                        onClick = { onChange(value); expanded = false }
                    )
                }
            }
        }
    }
}

// ── Text Field ───────────────────────────────────────────────────────────────
@Composable
fun ZHTextField(label: String, value: String, placeholder: String, onChange: (String) -> Unit) {
    Column {
        Text(label, color = TextDim, fontSize = 9.sp, fontFamily = FontFamily.Monospace, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 4.dp))
        OutlinedTextField(
            value         = value,
            onValueChange = onChange,
            placeholder   = { Text(placeholder, color = TextDim, fontSize = 12.sp, fontFamily = FontFamily.Monospace) },
            colors        = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = Gold,
                unfocusedBorderColor = Border,
                focusedTextColor     = TextBright,
                unfocusedTextColor   = TextBright,
                focusedContainerColor   = BgDeep,
                unfocusedContainerColor = BgDeep
            ),
            textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace, fontSize = 12.sp),
            modifier  = Modifier.fillMaxWidth()
        )
    }
}

// ── Alert Row ────────────────────────────────────────────────────────────────
@Composable
fun AlertRow(text: String, isError: Boolean = true) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp))
            .background(if (isError) RedBg else GoldDim)
            .border(1.dp, if (isError) Red.copy(alpha = .3f) else Gold.copy(alpha = .3f), RoundedCornerShape(4.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, color = if (isError) Red else Gold, fontSize = 11.sp, fontFamily = FontFamily.Monospace, lineHeight = 16.sp)
    }
    Spacer(Modifier.height(6.dp))
}

// ── Info Row ─────────────────────────────────────────────────────────────────
@Composable
fun InfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
        Text(label, color = Gold, fontSize = 10.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.width(90.dp))
        Text(value, color = TextMid, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
    }
    Divider(color = Border, thickness = 1.dp)
}

// ── Empty State ───────────────────────────────────────────────────────────────
@Composable
fun EmptyState(title: String, sub: String, onAction: (() -> Unit)? = null) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("◇", color = TextDim, fontSize = 32.sp)
        Spacer(Modifier.height(8.dp))
        Text(title, color = TextDim, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
        Text(sub,   color = TextDim, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        if (onAction != null) {
            Spacer(Modifier.height(10.dp))
            TextButton(onClick = onAction) { Text("GO THERE", color = Gold, fontSize = 10.sp, fontFamily = FontFamily.Monospace) }
        }
    }
}

// ── Tag ───────────────────────────────────────────────────────────────────────
@Composable
fun ZHTag(
    text: String,
    tagColorGold: Boolean = false, tagColorGreen: Boolean = false,
    tagColorRed:  Boolean = false, tagColorOrange: Boolean = false,
    tagColorGray: Boolean = false
) {
    val bg = when { tagColorGold -> GoldDim; tagColorGreen -> GreenBg; tagColorRed -> RedBg; tagColorOrange -> OrangeBg; else -> Color(0x26646464) }
    val fg = when { tagColorGold -> Gold; tagColorGreen -> Green; tagColorRed -> Red; tagColorOrange -> Orange; else -> TextDim }
    val border = when { tagColorGold -> GoldBorder; tagColorGreen -> Green.copy(alpha=.3f); tagColorRed -> Red.copy(alpha=.3f); tagColorOrange -> Orange.copy(alpha=.3f); else -> Border }
    Box(
        modifier = Modifier.clip(RoundedCornerShape(3.dp)).background(bg).border(1.dp, border, RoundedCornerShape(3.dp)).padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text, color = fg, fontSize = 8.sp, fontFamily = FontFamily.Monospace, letterSpacing = 1.sp)
    }
}

// ── CircleShape shorthand ────────────────────────────────────────────────────
private val androidx.compose.foundation.shape.CircleShape get() = RoundedCornerShape(50)
