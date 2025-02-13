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
            AppNavigation() // ⭐ 启动导航
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController() // ⭐ 创建导航控制器

    NavHost(navController, startDestination = "home") { // ⭐ 设置默认页面
        composable("home") { HomeScreen(navController) } // ⭐ 传递 navController
        composable("add_record") { AddRecordScreen(navController) } // ⭐ 这里修正，传入 navController
        composable("transactions") { TransactionListScreen(navController) } // ⭐ 这里修正，传入 navController
        composable("statistics") { StatisticsScreen(navController) } // ⭐ 这里修正，传入 navController
        composable("balance") { BalanceScreen(navController) }
    }
}
