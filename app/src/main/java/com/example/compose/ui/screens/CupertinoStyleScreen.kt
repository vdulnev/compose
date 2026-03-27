package com.example.compose.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// iOS-inspired color palette
private val CupertinoBlue = Color(0xFF007AFF)
private val CupertinoGreen = Color(0xFF34C759)
private val CupertinoRed = Color(0xFFFF3B30)
private val CupertinoOrange = Color(0xFFFF9500)
private val CupertinoGray = Color(0xFF8E8E93)
private val CupertinoLightGray = Color(0xFFF2F2F7)
private val CupertinoDivider = Color(0xFFC6C6C8)
private val CupertinoGroupedBg = Color(0xFFFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CupertinoStyleScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cupertino Style") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(CupertinoLightGray)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            CupertinoToggleSection()
            CupertinoSegmentedControlSection()
            CupertinoListSection()
            CupertinoSliderSection()
            CupertinoStepperSection()
            CupertinoActionButtonsSection()
            Spacer(Modifier.height(16.dp))
        }
    }
}

// --- iOS Toggle (Switch) ---

@Composable
private fun CupertinoToggleSection() {
    CupertinoSectionHeader("Toggle (Switch)")
    CupertinoGroupedCard {
        var wifiOn by remember { mutableStateOf(true) }
        var bluetoothOn by remember { mutableStateOf(false) }
        var airplaneOn by remember { mutableStateOf(false) }

        CupertinoToggleRow(label = "Wi-Fi", checked = wifiOn, onToggle = { wifiOn = it })
        CupertinoInsetDivider()
        CupertinoToggleRow(label = "Bluetooth", checked = bluetoothOn, onToggle = { bluetoothOn = it })
        CupertinoInsetDivider()
        CupertinoToggleRow(label = "Airplane Mode", checked = airplaneOn, onToggle = { airplaneOn = it })
    }
}

@Composable
private fun CupertinoToggleRow(label: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, fontSize = 17.sp)
        CupertinoSwitch(checked = checked, onCheckedChange = onToggle)
    }
}

@Composable
private fun CupertinoSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val trackColor by animateColorAsState(
        targetValue = if (checked) CupertinoGreen else Color(0xFFE9E9EA),
        animationSpec = tween(200),
        label = "track",
    )
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 20.dp else 0.dp,
        animationSpec = tween(200),
        label = "thumb",
    )

    Box(
        modifier = Modifier
            .size(width = 51.dp, height = 31.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(trackColor)
            .clickable { onCheckedChange(!checked) }
            .padding(2.dp),
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(27.dp)
                .shadow(2.dp, CircleShape)
                .background(Color.White, CircleShape),
        )
    }
}

// --- iOS Segmented Control ---

@Composable
private fun CupertinoSegmentedControlSection() {
    CupertinoSectionHeader("Segmented Control")
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("First", "Second", "Third")

    CupertinoGroupedCard {
        Column(modifier = Modifier.padding(16.dp)) {
            CupertinoSegmentedControl(
                options = options,
                selectedIndex = selectedIndex,
                onSelectionChanged = { selectedIndex = it },
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "Selected: ${options[selectedIndex]}",
                fontSize = 15.sp,
                color = CupertinoGray,
            )
        }
    }
}

@Composable
private fun CupertinoSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelectionChanged: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFE9E9EA))
            .padding(2.dp),
    ) {
        options.forEachIndexed { index, label ->
            val isSelected = index == selectedIndex
            val bgColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else Color.Transparent,
                animationSpec = tween(150),
                label = "seg$index",
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(7.dp))
                    .then(
                        if (isSelected) Modifier.shadow(1.dp, RoundedCornerShape(7.dp))
                        else Modifier
                    )
                    .background(bgColor)
                    .clickable { onSelectionChanged(index) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    label,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                )
            }
        }
    }
}

// --- iOS Grouped List ---

@Composable
private fun CupertinoListSection() {
    CupertinoSectionHeader("Grouped List")
    CupertinoGroupedCard {
        CupertinoListRow(label = "General", detail = "")
        CupertinoInsetDivider()
        CupertinoListRow(label = "Privacy", detail = "")
        CupertinoInsetDivider()
        CupertinoListRow(label = "Notifications", detail = "Banners, Sounds")
        CupertinoInsetDivider()
        CupertinoListRow(label = "Display & Brightness", detail = "")
        CupertinoInsetDivider()
        CupertinoListRow(label = "Battery", detail = "87%")
    }
}

@Composable
private fun CupertinoListRow(label: String, detail: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, fontSize = 17.sp, modifier = Modifier.weight(1f))
        if (detail.isNotEmpty()) {
            Text(detail, fontSize = 17.sp, color = CupertinoGray)
            Spacer(Modifier.width(4.dp))
        }
        Icon(
            Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = Color(0xFFC7C7CC),
            modifier = Modifier.size(14.dp),
        )
    }
}

// --- iOS Slider ---

@Composable
private fun CupertinoSliderSection() {
    CupertinoSectionHeader("Slider")
    var value by remember { mutableFloatStateOf(0.5f) }

    CupertinoGroupedCard {
        Column(modifier = Modifier.padding(16.dp)) {
            CupertinoSlider(value = value, onValueChange = { value = it })
            Spacer(Modifier.height(8.dp))
            Text(
                "Brightness: ${(value * 100).toInt()}%",
                fontSize = 15.sp,
                color = CupertinoGray,
            )
        }
    }
}

@Composable
private fun CupertinoSlider(value: Float, onValueChange: (Float) -> Unit) {
    // Use the Material slider styled to look iOS-like
    androidx.compose.material3.Slider(
        value = value,
        onValueChange = onValueChange,
        colors = androidx.compose.material3.SliderDefaults.colors(
            thumbColor = Color.White,
            activeTrackColor = CupertinoBlue,
            inactiveTrackColor = Color(0xFFE0E0E0),
        ),
        modifier = Modifier.fillMaxWidth(),
    )
}

// --- iOS Stepper ---

@Composable
private fun CupertinoStepperSection() {
    CupertinoSectionHeader("Stepper")
    var quantity by remember { mutableIntStateOf(1) }

    CupertinoGroupedCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Quantity", fontSize = 17.sp)
            CupertinoPlusMinus(
                value = quantity,
                onValueChange = { quantity = it },
                range = 0..99,
            )
        }
    }
}

@Composable
private fun CupertinoPlusMinus(value: Int, onValueChange: (Int) -> Unit, range: IntRange) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        Text(
            "$value",
            fontSize = 17.sp,
            modifier = Modifier.padding(end = 12.dp),
        )
        Row(
            modifier = Modifier
                .border(1.dp, CupertinoDivider, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
        ) {
            val minusEnabled = value > range.first
            val plusEnabled = value < range.last
            Box(
                modifier = Modifier
                    .size(width = 44.dp, height = 32.dp)
                    .clickable(enabled = minusEnabled) { onValueChange(value - 1) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "−",
                    fontSize = 20.sp,
                    color = if (minusEnabled) CupertinoBlue else CupertinoGray,
                )
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(32.dp)
                    .background(CupertinoDivider),
            )
            Box(
                modifier = Modifier
                    .size(width = 44.dp, height = 32.dp)
                    .clickable(enabled = plusEnabled) { onValueChange(value + 1) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "+",
                    fontSize = 20.sp,
                    color = if (plusEnabled) CupertinoBlue else CupertinoGray,
                )
            }
        }
    }
}

// --- iOS Action Buttons ---

@Composable
private fun CupertinoActionButtonsSection() {
    CupertinoSectionHeader("Action Buttons")
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        CupertinoButton(text = "Default Action", color = CupertinoBlue)
        CupertinoButton(text = "Destructive Action", color = CupertinoRed)
        CupertinoButton(text = "Tinted Action", color = CupertinoOrange)

        // Filled button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CupertinoBlue)
                .clickable { }
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text("Filled Button", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        }

        // Gray filled button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE9E9EA))
                .clickable { }
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text("Gray Button", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun CupertinoButton(text: String, color: Color) {
    CupertinoGroupedCard {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(text, color = color, fontSize = 17.sp)
        }
    }
}

// --- Shared building blocks ---

@Composable
private fun CupertinoSectionHeader(title: String) {
    Text(
        title.uppercase(),
        fontSize = 13.sp,
        color = CupertinoGray,
        fontWeight = FontWeight.Normal,
        modifier = Modifier.padding(start = 16.dp, bottom = 4.dp),
    )
}

@Composable
private fun CupertinoGroupedCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(CupertinoGroupedBg),
    ) {
        content()
    }
}

@Composable
private fun CupertinoInsetDivider() {
    HorizontalDivider(
        color = CupertinoDivider,
        modifier = Modifier.padding(start = 16.dp),
    )
}

@Composable
private fun CupertinoTextRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, fontSize = 17.sp)
        Text(value, fontSize = 17.sp, color = CupertinoGray, textAlign = TextAlign.End)
    }
}
