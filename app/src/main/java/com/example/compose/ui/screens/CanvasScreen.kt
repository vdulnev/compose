package com.example.compose.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CanvasScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Canvas") },
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
            ShapesSection()
            HorizontalDivider()
            BarChartSection()
            HorizontalDivider()
            AnimatedArcSection()
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ShapesSection() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Basic shapes", style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary)
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
        ) {
            // Circle
            drawCircle(
                color = Color(0xFF6200EE),
                radius = 40.dp.toPx(),
                center = Offset(70.dp.toPx(), size.height / 2),
            )
            // Rectangle
            drawRect(
                brush = Brush.linearGradient(listOf(Color(0xFF03DAC5), Color(0xFF018786))),
                topLeft = Offset(140.dp.toPx(), size.height / 2 - 35.dp.toPx()),
                size = Size(80.dp.toPx(), 70.dp.toPx()),
            )
            // Line
            drawLine(
                color = Color(0xFFFF5722),
                start = Offset(240.dp.toPx(), 20.dp.toPx()),
                end = Offset(size.width - 16.dp.toPx(), size.height - 20.dp.toPx()),
                strokeWidth = 6.dp.toPx(),
                cap = StrokeCap.Round,
            )
            // Triangle path
            val triX = 20.dp.toPx()
            val triY = size.height - 20.dp.toPx()
            drawPath(
                path = Path().apply {
                    moveTo(triX + 30.dp.toPx(), triY - 60.dp.toPx())
                    lineTo(triX, triY)
                    lineTo(triX + 60.dp.toPx(), triY)
                    close()
                },
                color = Color(0xFFFF9800),
            )
        }
    }
}

@Composable
private fun BarChartSection() {
    val values = listOf(0.4f, 0.75f, 0.55f, 0.9f, 0.6f, 0.3f, 0.8f)
    val labels = listOf("M", "T", "W", "T", "F", "S", "S")
    val textMeasurer = rememberTextMeasurer()
    val barColor = MaterialTheme.colorScheme.primary

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Bar chart", style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary)
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
        ) {
            val barWidth = size.width / (values.size * 2f)
            val chartHeight = size.height - 28.dp.toPx()
            values.forEachIndexed { i, value ->
                val barHeight = chartHeight * value
                val x = i * (size.width / values.size) + barWidth / 2
                drawRect(
                    color = barColor.copy(alpha = 0.15f + value * 0.85f),
                    topLeft = Offset(x, chartHeight - barHeight),
                    size = Size(barWidth, barHeight),
                )
                val textResult = textMeasurer.measure(
                    labels[i],
                    TextStyle(fontSize = 10.sp),
                )
                drawText(
                    textResult,
                    topLeft = Offset(x + barWidth / 2 - textResult.size.width / 2,
                        chartHeight + 4.dp.toPx()),
                )
            }
        }
    }
}

@Composable
private fun AnimatedArcSection() {
    val transition = rememberInfiniteTransition(label = "arc")
    val sweep by transition.animateFloat(
        initialValue = 30f,
        targetValue = 330f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing),
            RepeatMode.Reverse),
        label = "sweep",
    )
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Animated arc", style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary)
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
        ) {
            val radius = minOf(size.width, size.height) / 2f - 20.dp.toPx()
            val center = Offset(size.width / 2, size.height / 2)
            drawCircle(color = primary.copy(alpha = 0.1f), radius = radius, center = center)
            drawArc(
                brush = Brush.sweepGradient(listOf(primary, secondary), center),
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round),
            )
        }
    }
}
