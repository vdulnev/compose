package com.example.compose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GesturesScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestures") },
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
            DragSection()
            HorizontalDivider()
            TapSection()
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DragSection() {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Drag (detectDragGestures)", style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary)
        Text(
            "x=${offsetX.roundToInt()}  y=${offsetY.roundToInt()}",
            style = MaterialTheme.typography.bodySmall,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .background(Color(0xFF6200EE), CircleShape)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val maxX = with(density) { (300 - 64).dp.toPx() }
                            val maxY = with(density) { (220 - 64).dp.toPx() }
                            offsetX = (offsetX + dragAmount.x).coerceIn(0f, maxX)
                            offsetY = (offsetY + dragAmount.y).coerceIn(0f, maxY)
                        }
                    }
                    .align(Alignment.TopStart),
            )
        }
    }
}

@Composable
private fun TapSection() {
    var tapPosition by remember { mutableStateOf<Offset?>(null) }
    var longPressed by remember { mutableStateOf(false) }
    var doubleTapped by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Tap gestures (detectTapGestures)", style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary)
        Text(buildString {
            append("Tap: ${tapPosition?.let { "(${it.x.roundToInt()}, ${it.y.roundToInt()})" } ?: "—"}  ")
            append("Double: $doubleTapped  ")
            append("LongPress: $longPressed")
        }, style = MaterialTheme.typography.bodySmall)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(12.dp))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            tapPosition = offset
                            longPressed = false
                            doubleTapped = false
                        },
                        onDoubleTap = {
                            doubleTapped = true
                            longPressed = false
                        },
                        onLongPress = {
                            longPressed = true
                            doubleTapped = false
                        },
                    )
                },
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Tap, double-tap, or long-press",
                    color = MaterialTheme.colorScheme.onTertiaryContainer)
                if (longPressed) Text("Long press!", color = MaterialTheme.colorScheme.error)
                if (doubleTapped) Text("Double tap!", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
