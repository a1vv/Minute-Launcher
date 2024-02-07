package com.example.android.minutelauncher

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.android.minutelauncher.home.HomeScreen

@Composable
fun LauncherNavHost(
  navController: NavHostController,
) {
  NavHost(navController = navController, startDestination = "main") {
    composable("main") {
      HomeScreen()
    }
  }
}