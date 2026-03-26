package com.example.compose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Layouts") },
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Spacer(Modifier.height(4.dp))
            SectionTitle("Column — vertical arrangement")
            ColumnDemo()
            SectionTitle("Row — horizontal arrangement")
            RowDemo()
            SectionTitle("Box — layered stacking")
            BoxDemo()
            SectionTitle("Weight — proportional sizing")
            WeightDemo()
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ColumnDemo() {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        listOf(Color(0xFFEF9A9A), Color(0xFFF48FB1), Color(0xFFCE93D8)).forEach { color ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(color, RoundedCornerShape(8.dp)),
            )
        }
    }
}

@Composable
private fun RowDemo() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        listOf(Color(0xFF80DEEA), Color(0xFFA5D6A7), Color(0xFFFFF59D)).forEach { color ->
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(color, RoundedCornerShape(8.dp)),
            )
        }
    }
}

@Composable
private fun BoxDemo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF7986CB), RoundedCornerShape(12.dp)),
        )
        Text(
            "Bottom start",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp),
        )
        Text(
            "Top end",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp),
        )
        Surface(
            color = Color.White.copy(alpha = 0.3f),
            shape = RoundedCornerShape(50),
            modifier = Modifier.align(Alignment.Center),
        ) {
            Text("Center", modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp))
        }
    }
}

@Composable
private fun WeightDemo() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .background(Color(0xFFFFCC80), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center,
        ) { Text("weight(1)") }
        Box(
            modifier = Modifier
                .weight(2f)
                .height(50.dp)
                .background(Color(0xFFFFB74D), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center,
        ) { Text("weight(2)") }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
    )
}
