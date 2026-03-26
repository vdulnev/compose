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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
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
            HorizontalDivider()
            MutationPolicySection()
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CounterSection() {
    var count by remember { mutableIntStateOf(0) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "remember + mutableStateOf", style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
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
        Text(
            "rememberSaveable — survives rotation", style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
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
        Text(
            "State hoisting — stateless child", style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = if (isOn) "Light is ON" else "Light is OFF",
            style = MaterialTheme.typography.bodyMedium,
        )
        ToggleButton(isOn = isOn, onToggle = { isOn = !isOn })
    }
}

@Composable
private fun DerivedStateSection() {
    val items = remember { mutableStateListOf<Int>() }
    val evenCount by remember { derivedStateOf { items.count { it % 2 == 0 } } }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "derivedStateOf — computed value",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            "Items: ${items.joinToString()} — Even count: $evenCount",
            style = MaterialTheme.typography.bodyMedium,
        )
        Button(onClick = { items.add((1..10).random()) }) {
            Text("Add random number")
        }
    }
}

// Custom policy: treat floats as equal if they round to the same integer.
// Recomposition only happens when the rounded value actually changes.
private val RoundedFloatPolicy = object : SnapshotMutationPolicy<Float> {
    override fun equivalent(a: Float, b: Float): Boolean =
        a.roundToInt() == b.roundToInt()
}

@Composable
private fun MutationPolicySection() {
    // Default policy: structuralEqualityPolicy (==)
    // Setting the same value again does NOT trigger recomposition.
    var defaultCount by remember { mutableIntStateOf(0) }
    val defaultRecompositions = remember { intArrayOf(0) }

    // Custom policy: only recomposes when rounded value changes.
    // Raw value is stored in a plain array so it always accumulates;
    // the policy-guarded state only triggers recomposition when the
    // rounded integer changes — which is exactly the point of the demo.
    var rawTemperature by remember { mutableFloatStateOf(20.0f) }
    var temperature by remember {
        mutableStateOf(20.0f, RoundedFloatPolicy)
    }
    val customRecompositions = remember { intArrayOf(0) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            "SnapshotMutationPolicy",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )

        // Example 1: Default structural equality
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                "Default (structuralEqualityPolicy)",
                style = MaterialTheme.typography.labelLarge,
            )
            SideEffect { defaultRecompositions[0]++ }
            Text(
                "Value: $defaultCount" +
                        " — Recompositions: ${defaultRecompositions[0]}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { defaultCount++ }) {
                    Text("Change value")
                }
                Button(onClick = { defaultCount = defaultCount }) {
                    Text("Set same value")
                }
            }
        }

        // Example 2: Custom rounded float policy
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                "Custom (RoundedFloatPolicy)",
                style = MaterialTheme.typography.labelLarge,
            )
            SideEffect { customRecompositions[0]++ }
            Text(
                "Temp — Rounded: ${temperature.roundToInt()}°" +
                        " — Recompositions: ${customRecompositions[0]}",
                style = MaterialTheme.typography.bodyMedium,
            )
            RawTempText { rawTemperature }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    rawTemperature += 0.1f
                    temperature = rawTemperature
                }) {
                    Text("+0.1°")
                }
                Button(onClick = {
                    rawTemperature += 1.0f
                    temperature = rawTemperature
                }) {
                    Text("+1.0°")
                }
            }
        }
    }
}

@Composable
private fun RawTempText(rawTemperature: () -> Float) {
    Text(
        "Raw Temp: ${"%.1f".format(rawTemperature())}°",
        style = MaterialTheme.typography.bodyMedium,
    )
}
