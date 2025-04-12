package com.example.financetrackerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.financetrackerapp.ui.HomeScreen
import com.example.financetrackerapp.ui.AddRecordScreen
import com.example.financetrackerapp.ui.TransactionListScreen
import com.example.financetrackerapp.ui.StatisticsScreen
import com.example.financetrackerapp.ui.BalanceScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("add_record") { AddRecordScreen(navController) }
        composable("transactions") { TransactionListScreen(navController) }
        composable("statistics") { StatisticsScreen(navController) }
        composable("balance") { BalanceScreen(navController) }
    }
}
