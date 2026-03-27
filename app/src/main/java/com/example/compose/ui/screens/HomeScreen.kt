package com.example.compose.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.navigation.Routes

private data class Feature(
    val title: String,
    val subtitle: String,
    val route: String,
    val color: Color,
)

private val features = listOf(
    Feature("Layouts", "Column · Row · Box · Weight", Routes.LAYOUTS, Color(0xFF6200EE)),
    Feature("State", "remember · hoisting · derived", Routes.STATE, Color(0xFF00897B)),
    Feature("Animations", "visibility · color · content", Routes.ANIMATIONS, Color(0xFFE64A19)),
    Feature("Material 3", "Button · TextField · Card · Chip", Routes.MATERIAL, Color(0xFF388E3C)),
    Feature("Lazy Lists", "LazyColumn · headers · items", Routes.LAZY_LIST, Color(0xFF1976D2)),
    Feature("Gestures", "drag · long press · pointer", Routes.GESTURES, Color(0xFFF57F17)),
    Feature("Canvas", "shapes · paths · custom UI", Routes.CANVAS, Color(0xFF7B1FA2)),
    Feature("Side Effects", "LaunchedEffect · coroutines", Routes.SIDE_EFFECTS, Color(0xFF5D4037)),
    Feature("Cupertino Style", "iOS toggle · segmented · list", Routes.CUPERTINO, Color(0xFF607D8B)),
    Feature("Advanced M3", "Tabs · Sheet · Dialog · FAB", Routes.ADVANCED_MATERIAL, Color(0xFFC2185B)),
    Feature("Shaders", "Aurora · Liquid · Voronoi", Routes.SHADERS, Color(0xFF1A237E)),
    Feature("Scroll Effects", "Collapsing bar · sticky · fill", Routes.SCROLL_EFFECTS, Color(0xFF00695C)),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jetpack Compose Demo") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        },
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            items(features) { feature ->
                FeatureCard(feature = feature, onClick = { navController.navigate(feature.route) })
            }
        }
    }
}

@Composable
private fun FeatureCard(feature: Feature, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        colors = CardDefaults.cardColors(containerColor = feature.color),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = feature.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
            )
            Text(
                text = feature.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.85f),
            )
        }
    }
}
