package com.Madhu.moodtracker.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.Madhu.moodtracker.ui.DetailScreen
import com.Madhu.moodtracker.ui.MainScreen
import com.Madhu.moodtracker.ui.WeeklyReportScreen
import com.Madhu.moodtracker.viewmodel.MoodViewModel

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Detail : Screen("detail/{moodId}") {
        fun createRoute(moodId: Long) = "detail/$moodId"
    }
    object WeeklyReport : Screen("weekly_report")
}

@Composable
fun MoodTrackerNavigation() {
    val navController = rememberNavController()
    val viewModel: MoodViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(
                viewModel = viewModel,
                onNavigateToDetail = { moodId ->
                    navController.navigate(Screen.Detail.createRoute(moodId))
                },
                onNavigateToReport = {
                    navController.navigate(Screen.WeeklyReport.route)
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("moodId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val moodId = backStackEntry.arguments?.getLong("moodId") ?: 0L
            DetailScreen(
                moodId = moodId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.WeeklyReport.route) {
            WeeklyReportScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}