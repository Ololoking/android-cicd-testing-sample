package com.pixelsmatter.testingsample.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pixelsmatter.testingsample.ui.screens.greeting.GreetingScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = GREETING_ROUTE
    ) {
        composable(GREETING_ROUTE) {
            GreetingScreen()
        }
    }
}

private const val GREETING_ROUTE = "greeting"

