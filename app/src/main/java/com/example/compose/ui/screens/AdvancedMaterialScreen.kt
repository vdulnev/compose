package com.example.compose.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AdvancedMaterialScreen(navController: NavController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Advanced Material 3") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            SegmentedButtonSection()
            HorizontalDivider()
            TabsSection()
            HorizontalDivider()
            ChipsSection()
            HorizontalDivider()
            CardVariantsSection()
            HorizontalDivider()
            FabSection()
            HorizontalDivider()
            RangeSliderSection()
            HorizontalDivider()
            DialogSection()
            HorizontalDivider()
            BottomSheetSection()
            HorizontalDivider()
            DatePickerSection()
            HorizontalDivider()
            DropdownMenuSection()
            HorizontalDivider()
            SnackbarSection(onShowSnackbar = {
                scope.launch { snackbarHostState.showSnackbar("This is a Snackbar!") }
            })
            HorizontalDivider()
            NavigationBarSection()
            HorizontalDivider()
            BottomAppBarSection()
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
    )
}

// --- Segmented Buttons ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SegmentedButtonSection() {
    SectionLabel("SegmentedButton")

    // Single choice
    var selectedIndex by remember { mutableIntStateOf(0) }
    val singleOptions = listOf("Day", "Week", "Month")
    Text("Single choice", style = MaterialTheme.typography.labelMedium)
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        singleOptions.forEachIndexed { index, label ->
            SegmentedButton(
                selected = index == selectedIndex,
                onClick = { selectedIndex = index },
                shape = SegmentedButtonDefaults.itemShape(index, singleOptions.size),
            ) {
                Text(label)
            }
        }
    }

    Spacer(Modifier.height(8.dp))

    // Multi choice
    var selectedMulti by remember { mutableStateOf(setOf(0)) }
    val multiOptions = listOf("Bold", "Italic", "Underline")
    Text("Multi choice", style = MaterialTheme.typography.labelMedium)
    MultiChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        multiOptions.forEachIndexed { index, label ->
            SegmentedButton(
                checked = index in selectedMulti,
                onCheckedChange = {
                    selectedMulti = if (index in selectedMulti)
                        selectedMulti - index else selectedMulti + index
                },
                shape = SegmentedButtonDefaults.itemShape(index, multiOptions.size),
            ) {
                Text(label)
            }
        }
    }
}

// --- Tabs ---

@Composable
private fun TabsSection() {
    SectionLabel("Tabs")
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Photos", "Videos", "Albums")

    TabRow(selectedTabIndex = selectedTab) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { selectedTab = index },
                text = { Text(title) },
            )
        }
    }
    Text(
        "Selected: ${tabs[selectedTab]}",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(top = 8.dp),
    )
}

// --- Chips ---

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChipsSection() {
    SectionLabel("Chips (Assist, Suggestion, Input)")

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        AssistChip(
            onClick = {},
            label = { Text("Assist") },
            leadingIcon = { Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.height(18.dp)) },
        )
        SuggestionChip(onClick = {}, label = { Text("Suggestion") })
        var inputSelected by remember { mutableStateOf(true) }
        InputChip(
            selected = inputSelected,
            onClick = { inputSelected = !inputSelected },
            label = { Text("Input Chip") },
            trailingIcon = {
                Icon(Icons.Default.Close, contentDescription = "Remove", modifier = Modifier.height(18.dp))
            },
        )
        SuggestionChip(onClick = {}, label = { Text("Recipe") })
        AssistChip(
            onClick = {},
            label = { Text("Navigate") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.height(18.dp)) },
        )
    }
}

// --- Card Variants ---

@Composable
private fun CardVariantsSection() {
    SectionLabel("Card variants")

    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("ElevatedCard", style = MaterialTheme.typography.titleMedium)
            Text("Has shadow elevation", style = MaterialTheme.typography.bodyMedium)
        }
    }
    Spacer(Modifier.height(8.dp))
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("OutlinedCard", style = MaterialTheme.typography.titleMedium)
            Text("Has a border outline", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// --- FABs ---

@Composable
private fun FabSection() {
    SectionLabel("FloatingActionButton")
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FloatingActionButton(onClick = {}) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
        ExtendedFloatingActionButton(
            onClick = {},
            icon = { Icon(Icons.Default.Edit, contentDescription = null) },
            text = { Text("Compose") },
        )
    }
}

// --- Range Slider ---

@Composable
private fun RangeSliderSection() {
    SectionLabel("RangeSlider")
    var range by remember { mutableStateOf(0.2f..0.8f) }
    Column {
        RangeSlider(
            value = range,
            onValueChange = { range = it },
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            "Range: ${"%.0f".format(range.start * 100)}% – ${"%.0f".format(range.endInclusive * 100)}%",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

// --- AlertDialog ---

@Composable
private fun DialogSection() {
    SectionLabel("AlertDialog")
    var showDialog by remember { mutableStateOf(false) }
    TextButton(onClick = { showDialog = true }) { Text("Show AlertDialog") }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            icon = { Icon(Icons.Default.Info, contentDescription = null) },
            title = { Text("Confirm action") },
            text = { Text("Are you sure you want to proceed with this action?") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            },
        )
    }
}

// --- Bottom Sheet ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetSection() {
    SectionLabel("ModalBottomSheet")
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    TextButton(onClick = { showSheet = true }) { Text("Show Bottom Sheet") }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text("Bottom Sheet", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "This is a modal bottom sheet. You can put any content here.",
                    style = MaterialTheme.typography.bodyMedium,
                )
                listOf("Share", "Link", "Edit", "Delete").forEach { action ->
                    Text(
                        action,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                    )
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

// --- DatePicker ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerSection() {
    SectionLabel("DatePicker")
    var showPicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Row(verticalAlignment = Alignment.CenterVertically) {
        TextButton(onClick = { showPicker = true }) { Text("Pick a date") }
        Spacer(Modifier.width(8.dp))
        val millis = datePickerState.selectedDateMillis
        if (millis != null) {
            val date = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
                .format(java.util.Date(millis))
            Text(date, style = MaterialTheme.typography.bodyMedium)
        }
    }

    if (showPicker) {
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = { showPicker = false }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) { Text("Cancel") }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// --- DropdownMenu ---

@Composable
private fun DropdownMenuSection() {
    SectionLabel("DropdownMenu")
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf("Select an option") }

    Column {
        TextButton(onClick = { expanded = true }) {
            Text(selected)
            Spacer(Modifier.width(4.dp))
            Icon(Icons.Default.DateRange, contentDescription = null)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            listOf("Option A", "Option B", "Option C", "Option D").forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selected = option
                        expanded = false
                    },
                )
            }
        }
    }
}

// --- Snackbar ---

@Composable
private fun SnackbarSection(onShowSnackbar: () -> Unit) {
    SectionLabel("Snackbar")
    TextButton(onClick = onShowSnackbar) { Text("Show Snackbar") }
}

// --- NavigationBar ---

@Composable
private fun NavigationBarSection() {
    SectionLabel("NavigationBar")
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(
        "Home" to Icons.Default.Home,
        "Search" to Icons.Default.Search,
        "Favorites" to Icons.Default.Favorite,
        "Settings" to Icons.Default.Settings,
    )

    NavigationBar {
        items.forEachIndexed { index, (label, icon) ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { selectedItem = index },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) },
            )
        }
    }
}

// --- BottomAppBar ---

@Composable
private fun BottomAppBarSection() {
    SectionLabel("BottomAppBar")
    BottomAppBar(
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Check, contentDescription = "Check")
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
    )
}
