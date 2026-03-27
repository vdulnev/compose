package com.example.compose.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.IntSize
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
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "AnimatedVisibility",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        Button(onClick = { visible = !visible }) {
            Text(if (visible) "Hide all" else "Show all")
        }

        // 1. Fade + expand vertically (default-like)
        VisibilityVariant(
            label = "fadeIn + expandVertically",
            visible = visible,
            enter = fadeIn(tween(500)) + expandVertically(),
            exit = fadeOut(tween(500)) + shrinkVertically(),
            color = Color(0xFF7986CB),
        )

        // 2. Slide in from the left
        VisibilityVariant(
            label = "slideInHorizontally",
            visible = visible,
            enter = slideInHorizontally { -it },
            exit = slideOutHorizontally { -it },
            color = Color(0xFF4CAF50),
        )

        // 3. Slide in from the bottom
        VisibilityVariant(
            label = "slideInVertically (from bottom)",
            visible = visible,
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
            color = Color(0xFFFF7043),
        )

        // 4. Scale in/out
        VisibilityVariant(
            label = "scaleIn + fadeIn",
            visible = visible,
            enter = scaleIn(tween(400)) + fadeIn(tween(400)),
            exit = scaleOut(tween(400)) + fadeOut(tween(400)),
            color = Color(0xFF9C27B0),
        )

        // 5. Expand horizontally from start
        VisibilityVariant(
            label = "expandHorizontally",
            visible = visible,
            enter = expandHorizontally(expandFrom = Alignment.Start),
            exit = shrinkHorizontally(shrinkTowards = Alignment.Start),
            color = Color(0xFF00BCD4),
        )

        // 6. Combined: slide + fade + scale
        VisibilityVariant(
            label = "slide + fade + scale (combined)",
            visible = visible,
            enter = slideInHorizontally { it / 2 } + fadeIn() + scaleIn(initialScale = 0.8f),
            exit = slideOutHorizontally { it / 2 } + fadeOut() + scaleOut(targetScale = 0.8f),
            color = Color(0xFFE91E63),
        )
    }
}

@Composable
private fun VisibilityVariant(
    label: String,
    visible: Boolean,
    enter: EnterTransition,
    exit: ExitTransition,
    color: Color,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        AnimatedVisibility(
            visible = visible,
            enter = enter,
            exit = exit,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(color, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(label, color = Color.White)
            }
        }
    }
}

@Composable
private fun AnimateColorSection() {
    var isActive by remember { mutableStateOf(false) }

    // 1. tween — smooth linear interpolation
    val tweenColor by animateColorAsState(
        targetValue = if (isActive) Color(0xFF4CAF50) else Color(0xFFEF5350),
        animationSpec = tween(durationMillis = 600),
        label = "tween",
    )

    // 2. spring — physics-based, can overshoot
    val springColor by animateColorAsState(
        targetValue = if (isActive) Color(0xFF2196F3) else Color(0xFFFF9800),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "spring",
    )

    // 3. keyframes — multi-step with intermediate colors
    val keyframeColor by animateColorAsState(
        targetValue = if (isActive) Color(0xFF9C27B0) else Color(0xFF607D8B),
        animationSpec = keyframes {
            durationMillis = 1000
            Color(0xFFFFEB3B) atFraction 0.25f
            Color(0xFFE91E63) atFraction 0.5f
            Color(0xFF00BCD4) atFraction 0.75f
        },
        label = "keyframes",
    )

    // 4. repeatable — bounces back and forth a few times
    val repeatColor by animateColorAsState(
        targetValue = if (isActive) Color(0xFF8BC34A) else Color(0xFF795548),
        animationSpec = repeatable(
            iterations = 3,
            animation = tween(300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "repeatable",
    )

    // 5. tween with slow easing — text + border color
    val textBorderColor by animateColorAsState(
        targetValue = if (isActive) Color(0xFF3F51B5) else Color(0xFFCDDC39),
        animationSpec = tween(durationMillis = 1200, easing = LinearEasing),
        label = "textBorder",
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "animateColorAsState",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        Button(onClick = { isActive = !isActive }) {
            Text(if (isActive) "Reset" else "Animate all")
        }

        ColorVariantBox(label = "tween (600ms)", color = tweenColor)
        ColorVariantBox(label = "spring (bouncy)", color = springColor)
        ColorVariantBox(label = "keyframes (multi-step)", color = keyframeColor)
        ColorVariantBox(label = "repeatable (3×reverse)", color = repeatColor)

        // 5. Text + border color instead of background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(3.dp, textBorderColor, RoundedCornerShape(12.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "Animated text & border color",
                style = MaterialTheme.typography.bodyLarge,
                color = textBorderColor,
            )
        }
    }
}

@Composable
private fun ColorVariantBox(label: String, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(color, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(label, color = Color.White)
    }
}

@Composable
private fun AnimateContentSizeSection() {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "animateContentSize",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        Button(onClick = { expanded = !expanded }) {
            Text(if (expanded) "Collapse all" else "Expand all")
        }

        // 1. Default (tween 300ms)
        ContentSizeVariant(
            label = "Default (tween)",
            expanded = expanded,
            animationSpec = tween<IntSize>(),
            color = MaterialTheme.colorScheme.surfaceVariant,
        )

        // 2. Spring — bouncy resize
        ContentSizeVariant(
            label = "Spring (bouncy)",
            expanded = expanded,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow,
            ),
            color = Color(0xFF7986CB).copy(alpha = 0.2f),
        )

        // 3. Slow tween — deliberate, visible interpolation
        ContentSizeVariant(
            label = "Slow tween (1200ms)",
            expanded = expanded,
            animationSpec = tween(durationMillis = 1200, easing = LinearEasing),
            color = Color(0xFF4CAF50).copy(alpha = 0.2f),
        )

        // 4. Spring stiff — snappy resize
        ContentSizeVariant(
            label = "Spring (stiff)",
            expanded = expanded,
            animationSpec = spring(stiffness = Spring.StiffnessHigh),
            color = Color(0xFFFF9800).copy(alpha = 0.2f),
        )

        // 5. Tween with delay — waits before resizing
        ContentSizeVariant(
            label = "Tween with delay (400ms)",
            expanded = expanded,
            animationSpec = tween(
                durationMillis = 600,
                delayMillis = 400,
            ),
            color = Color(0xFFE91E63).copy(alpha = 0.2f),
        )
    }
}

@Composable
private fun ContentSizeVariant(
    label: String,
    expanded: Boolean,
    animationSpec: androidx.compose.animation.core.FiniteAnimationSpec<IntSize>,
    color: Color,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color, RoundedCornerShape(12.dp))
                .animateContentSize(animationSpec = animationSpec)
                .padding(16.dp),
        ) {
            Text(
                text = if (expanded) {
                    "Jetpack Compose is a modern toolkit for building native Android UI. " +
                        "It simplifies and accelerates UI development with less code, " +
                        "powerful tools, and intuitive Kotlin APIs."
                } else {
                    label
                },
            )
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
