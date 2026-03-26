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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideEffectsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Side Effects") },
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
            LaunchedEffectSection()
            HorizontalDivider()
            CoroutineScopeSection()
            HorizontalDivider()
            DisposableEffectSection()
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun LaunchedEffectSection() {
    var running by remember { mutableStateOf(false) }
    var seconds by remember { mutableIntStateOf(0) }

    // LaunchedEffect restarts whenever `running` changes
    LaunchedEffect(running) {
        if (running) {
            while (true) {
                delay(1000L)
                seconds++
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("LaunchedEffect — coroutine tied to composition",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary)
        Text(
            "Elapsed: ${seconds}s",
            style = MaterialTheme.typography.headlineMedium,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { running = true }) { Text("Start") }
            OutlinedButton(onClick = {
                running = false
                seconds = 0
            }) { Text("Reset") }
        }
        Text(
            "LaunchedEffect(running) relaunches the coroutine every time 'running' changes. " +
                "The coroutine is cancelled when the composable leaves composition.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun CoroutineScopeSection() {
    val scope = rememberCoroutineScope()
    var log by remember { mutableStateOf("Press Launch to start a job") }
    var jobCount by remember { mutableIntStateOf(0) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("rememberCoroutineScope — manually launched coroutines",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary)
        Text(log, style = MaterialTheme.typography.bodyMedium)
        Button(onClick = {
            val id = ++jobCount
            scope.launch {
                log = "Job #$id started…"
                delay(2000L)
                log = "Job #$id finished after 2s"
            }
        }) { Text("Launch Job") }
        Text(
            "rememberCoroutineScope gives a scope bound to the composable's lifecycle, " +
                "useful for launching coroutines from callbacks (not composition).",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun DisposableEffectSection() {
    var mounted by remember { mutableStateOf(false) }
    var eventLog by remember { mutableStateOf(listOf<String>()) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("DisposableEffect — setup and teardown",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary)

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(onClick = { mounted = !mounted }) {
                Text(if (mounted) "Unmount" else "Mount")
            }
        }

        if (mounted) {
            DisposableEffect(Unit) {
                eventLog = eventLog + "Component mounted"
                onDispose {
                    eventLog = eventLog + "Component disposed"
                }
            }
            Text("Component is mounted", style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            eventLog.takeLast(6).forEach { entry ->
                Text("• $entry", style = MaterialTheme.typography.bodySmall)
            }
        }
        Text(
            "DisposableEffect runs on entry and its onDispose block runs on exit or when " +
                "the key changes — ideal for registering/unregistering listeners.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
