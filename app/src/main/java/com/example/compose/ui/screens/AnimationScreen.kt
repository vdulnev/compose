package com.example.compose.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimationScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Animations") },
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
            AnimatedVisibilitySection()
            HorizontalDivider()
            AnimateColorSection()
            HorizontalDivider()
            AnimateContentSizeSection()
            HorizontalDivider()
            InfiniteTransitionSection()
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun AnimatedVisibilitySection() {
    var visible by remember { mutableStateOf(true) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("AnimatedVisibility", style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary)
        Button(onClick = { visible = !visible }) {
            Text(if (visible) "Hide" else "Show")
        }
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color(0xFF7986CB), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text("I appear and disappear!", color = Color.White)
            }
        }
    }
}

@Composable
private fun AnimateColorSection() {
    var isActive by remember { mutableStateOf(false) }
    val bgColor by animateColorAsState(
        targetValue = if (isActive) Color(0xFF4CAF50) else Color(0xFFEF5350),
        animationSpec = tween(durationMillis = 500),
        label = "bgColor",
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("animateColorAsState", style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(bgColor, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(if (isActive) "Active" else "Inactive", color = Color.White)
        }
        Button(onClick = { isActive = !isActive }) { Text("Toggle") }
    }
}

@Composable
private fun AnimateContentSizeSection() {
    var expanded by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("animateContentSize", style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                .animateContentSize()
                .padding(16.dp),
        ) {
            Text(
                text = if (expanded) {
                    "Jetpack Compose is a modern toolkit for building native Android UI. " +
                        "It simplifies and accelerates UI development with less code, powerful tools, " +
                        "and intuitive Kotlin APIs."
                } else {
                    "Tap to expand..."
                },
            )
        }
        Button(onClick = { expanded = !expanded }) {
            Text(if (expanded) "Collapse" else "Expand")
        }
    }
}

@Composable
private fun InfiniteTransitionSection() {
    val transition = rememberInfiniteTransition(label = "infinite")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Restart,
        ),
        label = "rotation",
    )
    val scale by transition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "scale",
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("InfiniteTransition", style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth())
        Box(
            modifier = Modifier
                .size((60 * scale).dp)
                .rotate(rotation)
                .background(Color(0xFF9C27B0), RoundedCornerShape(12.dp)),
        )
        Text("Rotating & pulsing box", style = MaterialTheme.typography.bodySmall)
        // Pulsing circle
        Box(
            modifier = Modifier
                .size((50 * scale).dp)
                .background(Color(0xFF2196F3), CircleShape),
        )
    }
}
