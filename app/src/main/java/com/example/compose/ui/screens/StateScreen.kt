package com.example.compose.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("State") },
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
            CounterSection()
            HorizontalDivider()
            TextFieldSection()
            HorizontalDivider()
            StateHoistingSection()
            HorizontalDivider()
            DerivedStateSection()
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CounterSection() {
    var count by remember { mutableIntStateOf(0) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("remember + mutableStateOf", style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            IconButton(onClick = { count-- }) {
                Icon(Icons.Default.Remove, contentDescription = "Decrement")
            }
            Text("$count", style = MaterialTheme.typography.headlineMedium)
            IconButton(onClick = { count++ }) {
                Icon(Icons.Default.Add, contentDescription = "Increment")
            }
        }
    }
}

@Composable
private fun TextFieldSection() {
    var text by rememberSaveable { mutableStateOf("") }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("rememberSaveable — survives rotation", style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary)
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Type something") },
            modifier = Modifier.fillMaxWidth(),
        )
        Text("Characters: ${text.length}", style = MaterialTheme.typography.bodyMedium)
    }
}

// Stateless child — state hoisted to parent
@Composable
private fun ToggleButton(
    isOn: Boolean,
    onToggle: () -> Unit,
) {
    FilledTonalButton(onClick = onToggle) {
        Text(if (isOn) "ON" else "OFF")
    }
}

@Composable
private fun StateHoistingSection() {
    var isOn by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("State hoisting — stateless child", style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary)
        Text(
            text = if (isOn) "Light is ON" else "Light is OFF",
            style = MaterialTheme.typography.bodyMedium,
        )
        ToggleButton(isOn = isOn, onToggle = { isOn = !isOn })
    }
}

@Composable
private fun DerivedStateSection() {
    var count by remember { mutableIntStateOf(0) }
    val isEven by remember { derivedStateOf { count % 2 == 0 } }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("derivedStateOf — computed value", style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary)
        Text("Count: $count — ${if (isEven) "Even" else "Odd"}",
            style = MaterialTheme.typography.bodyMedium)
        Button(onClick = { count++ }) { Text("Increment") }
    }
}
