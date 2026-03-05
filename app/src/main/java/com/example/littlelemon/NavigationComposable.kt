package com.example.littlelemon

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

@Composable
fun MyNavigation(navController: NavHostController) {

    val context = LocalContext.current
    val sharedPreferences =
        context.getSharedPreferences("LittleLemon", Context.MODE_PRIVATE)

    val firstName = sharedPreferences.getString("firstName", "")

    val startDestination =
        if (firstName.isNullOrBlank())
            OnboardingDestination.route
        else
            HomeDestination.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(OnboardingDestination.route) {
            Onboarding(navController)
        }

        composable(HomeDestination.route) {
            Home(navController)
        }

        composable(ProfileDestination.route) {
            Profile(navController)
        }
    }
}