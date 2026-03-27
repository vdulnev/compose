package com.example.compose.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// ── Data ────────────────────────────────────────────────────────────────

private data class Category(val name: String, val color: Color)

private val categories = listOf(
    Category("Featured", Color(0xFFE91E63)),
    Category("Popular", Color(0xFF9C27B0)),
    Category("New", Color(0xFF3F51B5)),
    Category("Trending", Color(0xFF009688)),
    Category("Top Rated", Color(0xFFFF5722)),
    Category("Editors' Pick", Color(0xFF795548)),
)

private data class ContentItem(val id: Int, val title: String, val subtitle: String)

private val sectionItems = (1..8).map {
    ContentItem(it, "Item $it", "Description for item $it with some detail text")
}

// ── Screen ──────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ScrollEffectsScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()

    // Derived state: is the list scrolled past the hero?
    val isScrolled by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    val appBarContainerColor by animateColorAsState(
        targetValue = if (isScrolled)
            MaterialTheme.colorScheme.surfaceContainer
        else
            MaterialTheme.colorScheme.surface,
        label = "appBarColor",
    )

    Scaffold(
        topBar = {
            // ── SliverAppBar equivalent: collapsing LargeTopAppBar ──
            LargeTopAppBar(
                title = { Text("Scroll Effects") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = appBarContainerColor,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { padding ->
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 24.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            // ── SliverToBoxAdapter: hero banner ──
            item {
                HeroBanner()
            }

            // ── SliverToBoxAdapter: horizontal category chips (like a nested SliverList) ──
            item {
                CategoryRow()
            }

            // ── SliverPersistentHeader: sticky section header ──
            stickyHeader {
                SectionHeader(title = "Recent Items", emoji = "⏱")
            }

            // ── SliverList: list items with parallax-like effect ──
            items(sectionItems, key = { it.id }) { item ->
                ContentCard(item = item)
            }

            // ── Another sticky header ──
            stickyHeader {
                SectionHeader(title = "Favorites", emoji = "❤\uFE0F")
            }

            items(
                items = (9..16).map {
                    ContentItem(it, "Favorite $it", "Your favorite item #$it")
                },
                key = { it.id },
            ) { item ->
                FavoriteCard(item = item)
            }

            // ── SliverFillRemaining: footer that fills remaining space ──
            item {
                Box(
                    modifier = Modifier
                        .fillParentMaxHeight(0.3f)
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "fillParentMaxHeight",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            "≈ SliverFillRemaining",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                        )
                    }
                }
            }
        }
    }
}

// ── Components ──────────────────────────────────────────────────────────

@Composable
private fun HeroBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF667eea),
                        Color(0xFF764ba2),
                    ),
                ),
            ),
        contentAlignment = Alignment.BottomStart,
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Scroll Effects Demo",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Compose equivalents of Flutter Slivers",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f),
            )
        }
    }
}

@Composable
private fun CategoryRow() {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            "Categories",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        // ── Horizontal LazyRow ≈ SliverToBoxAdapter wrapping a horizontal ListView ──
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(categories) { category ->
                CategoryChip(category)
            }
        }
    }
}

@Composable
private fun CategoryChip(category: Category) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(category.color.copy(alpha = 0.15f))
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        Text(
            category.name,
            style = MaterialTheme.typography.labelLarge,
            color = category.color,
        )
    }
}

@Composable
private fun SectionHeader(title: String, emoji: String) {
    // ── stickyHeader ≈ SliverPersistentHeader(pinned: true) ──
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(emoji, fontSize = 18.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun ContentCard(item: ContentItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Numbered circle
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(2.dp, CircleShape)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "${item.id}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, style = MaterialTheme.typography.titleSmall)
                Text(
                    item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun FavoriteCard(item: ContentItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Default.Favorite,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(28.dp),
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, style = MaterialTheme.typography.titleSmall)
                Text(
                    item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .size(20.dp)
                    .alpha(0.5f),
            )
        }
    }
}
