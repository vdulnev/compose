package com.example.compose.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

private data class ListItem(val id: Int, val title: String, val body: String)

private fun sampleItem(id: Int) = ListItem(
    id = id,
    title = "Item #$id",
    body = "This is the body of item $id. Tap the delete icon to remove it.",
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LazyListScreen(navController: NavController) {
    val items = remember { mutableStateListOf(*Array(12) { sampleItem(it + 1) }) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // State for drag and drop reordering
    var dragIndex by remember { mutableIntStateOf(-1) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }

    val density = LocalDensity.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lazy Lists") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                items.add(0, sampleItem(items.size + 1))
                scope.launch { listState.animateScrollToItem(0) }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add item")
            }
        },
    ) { padding ->
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .animateContentSize(),
        ) {
            // Sticky header
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text(
                        "${items.size} items — tap + to add, trash to remove, long-press & drag to reorder",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }

            itemsIndexed(items = items, key = { _, item -> item.id }) { index, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem()
                        .zIndex(if (dragIndex == index) 1f else 0f)
                        .offset {
                            if (dragIndex == index) IntOffset(
                                0,
                                dragOffsetY.toInt()
                            ) else IntOffset(0, 0)
                        },
                    onClick = {},
                ) {
                    Row(
                        modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Drag to reorder",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .pointerInput(item.id) {
                                    detectDragGesturesAfterLongPress(
                                        onDragStart = {
                                            dragIndex = index
                                        },
                                        onDragEnd = {
                                            dragOffsetY = 0f
                                            dragIndex = -1
                                        },
                                        onDragCancel = {
                                            dragOffsetY = 0f
                                            dragIndex = -1
                                        },
                                        onDrag = { _, dragAmount ->
                                            dragOffsetY += dragAmount.y

                                            val itemHeight =
                                                with(density) { 100.dp.toPx() }

                                            val deltaItems = (dragOffsetY / itemHeight).toInt()

                                            if (deltaItems != 0 && dragIndex != -1) {
                                                val newDragIndex = dragIndex + deltaItems
                                                if (newDragIndex in 0 until items.size) {
                                                    val draggedItem = items.removeAt(dragIndex)
                                                    items.add(newDragIndex, draggedItem)
                                                    dragIndex = newDragIndex
                                                    dragOffsetY = 0f
                                                }
                                            }
                                        },
                                    )
                                },
                        )
                        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                            Text(item.title, style = MaterialTheme.typography.titleSmall)
                            Text(item.body, style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { items.remove(item) }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                }
            }
        }
    }
}
