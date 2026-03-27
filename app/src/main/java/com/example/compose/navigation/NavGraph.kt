package com.example.compose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.ui.screens.AdvancedMaterialScreen
import com.example.compose.ui.screens.AnimationScreen
import com.example.compose.ui.screens.CanvasScreen
import com.example.compose.ui.screens.CupertinoStyleScreen
import com.example.compose.ui.screens.GesturesScreen
import com.example.compose.ui.screens.HomeScreen
import com.example.compose.ui.screens.LazyListScreen
import com.example.compose.ui.screens.LayoutsScreen
import com.example.compose.ui.screens.MaterialComponentsScreen
import com.example.compose.ui.screens.SideEffectsScreen
import com.example.compose.ui.screens.StateScreen

object Routes {
    const val HOME = "home"
    const val LAYOUTS = "layouts"
    const val STATE = "state"
    const val ANIMATIONS = "animations"
    const val MATERIAL = "material"
    const val LAZY_LIST = "lazy_list"
    const val GESTURES = "gestures"
    const val CANVAS = "canvas"
    const val SIDE_EFFECTS = "side_effects"
    const val CUPERTINO = "cupertino"
    const val ADVANCED_MATERIAL = "advanced_material"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) { HomeScreen(navController) }
        composable(Routes.LAYOUTS) { LayoutsScreen(navController) }
        composable(Routes.STATE) { StateScreen(navController) }
        composable(Routes.ANIMATIONS) { AnimationScreen(navController) }
        composable(Routes.MATERIAL) { MaterialComponentsScreen(navController) }
        composable(Routes.LAZY_LIST) { LazyListScreen(navController) }
        composable(Routes.GESTURES) { GesturesScreen(navController) }
        composable(Routes.CANVAS) { CanvasScreen(navController) }
        composable(Routes.SIDE_EFFECTS) { SideEffectsScreen(navController) }
        composable(Routes.CUPERTINO) { CupertinoStyleScreen(navController) }
        composable(Routes.ADVANCED_MATERIAL) { AdvancedMaterialScreen(navController) }
    }
}
