package com.example.financetrackerapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.Locale
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.IconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceScreen(navController: NavController) {
    var totalBalance by remember { mutableStateOf(12345.78) } // Account balance
    val totalIncome by remember { mutableStateOf(50000.00) }  // Total income
    val totalExpense by remember { mutableStateOf(37654.22) } // Total expenses
    var savingGoal by remember { mutableStateOf(20000.00) }   // Default saving goal

    // Calculate saving goal progress
    val savingProgress = (totalBalance / savingGoal).coerceIn(0.0, 1.0) // **Ensure it's a Double**
    val progressColor = when {
        savingProgress >= 1.0 -> Color.Green  // Goal Achieved
        savingProgress >= 0.5 -> Color.Blue   // Midway Progress
        else -> Color.Red                    // Less than 50% progress
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Total Savings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display Total Balance
            Text("Account Balance:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Text(
                "¥ ${String.format(Locale.US, "%.2f", totalBalance)}",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()

            // Income & Expense Summary
            Spacer(modifier = Modifier.height(8.dp))
            Text("Income & Expense Summary", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text(
                "Total Income: ¥${String.format(Locale.US, "%.2f", totalIncome)}",
                color = Color.Green,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Total Expense: ¥${String.format(Locale.US, "%.2f", totalExpense)}",
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()

            // Saving Goal Section
            Spacer(modifier = Modifier.height(8.dp))
            Text("Saving Goal", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text("Target: ¥${String.format(Locale.US, "%.2f", savingGoal)}", fontWeight = FontWeight.Bold)

            // Progress Bar for Savings
            LinearProgressIndicator(
                progress = { savingProgress.toFloat() },  // **Fixed progress parameter**
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = progressColor
            )

            // User Input to Adjust Saving Goal
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = savingGoal.toString(),
                onValueChange = { newValue ->
                    newValue.toDoubleOrNull()?.let { savingGoal = it }
                },
                label = { Text("Set Saving Goal") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Button to Save Changes
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* TODO: Save new goal */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Adjust Saving Goal")
            }
        }
    }
}
