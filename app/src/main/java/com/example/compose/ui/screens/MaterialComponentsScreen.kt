package com.example.compose.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MaterialComponentsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Material 3") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Buttons
            Label("Buttons")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {}) { Text("Filled") }
                OutlinedButton(onClick = {}) { Text("Outlined") }
                TextButton(onClick = {}) { Text("Text") }
                FilledTonalButton(onClick = {}) { Text("Tonal") }
                ElevatedButton(onClick = {}) { Text("Elevated") }
            }
            HorizontalDivider()

            // TextField
            Label("TextField")
            var text by remember { mutableStateOf("") }
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Outlined TextField") },
                modifier = Modifier.fillMaxWidth(),
            )
            HorizontalDivider()

            // Card
            Label("Card")
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Card Title", style = MaterialTheme.typography.titleMedium)
                    Text("Cards contain content and actions about a single subject.",
                        style = MaterialTheme.typography.bodyMedium)
                }
            }
            HorizontalDivider()

            // Chips
            Label("FilterChip")
            val chipOptions = listOf("Android", "Compose", "Kotlin", "Material")
            var selectedChips by remember { mutableStateOf(setOf<String>()) }
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                chipOptions.forEach { label ->
                    FilterChip(
                        selected = label in selectedChips,
                        onClick = {
                            selectedChips = if (label in selectedChips)
                                selectedChips - label else selectedChips + label
                        },
                        label = { Text(label) },
                    )
                }
            }
            HorizontalDivider()

            // Switch, Checkbox, RadioButton
            Label("Selection controls")
            var switchOn by remember { mutableStateOf(true) }
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Switch(checked = switchOn, onCheckedChange = { switchOn = it })
                Text("Switch: ${if (switchOn) "On" else "Off"}")
            }
            var checked by remember { mutableStateOf(false) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = checked, onCheckedChange = { checked = it })
                Text("Checkbox")
            }
            var selected by remember { mutableIntStateOf(0) }
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf("A", "B", "C").forEachIndexed { index, label ->
                    RadioButton(selected = selected == index, onClick = { selected = index })
                    Text(label)
                }
            }
            HorizontalDivider()

            // Slider
            Label("Slider")
            var sliderValue by remember { mutableFloatStateOf(0.5f) }
            Column {
                Slider(value = sliderValue, onValueChange = { sliderValue = it })
                Text("Value: ${"%.2f".format(sliderValue)}")
            }
            HorizontalDivider()

            // Progress indicators
            Label("Progress indicators")
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator()
                CircularProgressIndicator(progress = { sliderValue })
            }
            HorizontalDivider()

            // Badge & IconToggleButton
            Label("Badge + IconToggleButton")
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically) {
                BadgedBox(badge = { Badge { Text("5") } }) {
                    Icon(Icons.Default.Notifications, contentDescription = null)
                }
                var liked by remember { mutableStateOf(false) }
                IconToggleButton(checked = liked, onCheckedChange = { liked = it }) {
                    Icon(
                        if (liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(text, style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary)
}
