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
import com.example.financetrackerapp.viewmodel.TransactionViewModel
import java.util.*
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceScreen(
    navController: NavController,
    viewModel: TransactionViewModel = viewModel()
) {
    val transactions by viewModel.transactions.collectAsState()
    val savingGoal by viewModel.savingGoal

    val totalIncome = transactions.filter { it.amount > 0 }.sumOf { it.amount }
    val totalExpense = transactions.filter { it.amount < 0 }.sumOf { it.amount * -1 }
    val totalBalance = totalIncome - totalExpense

    // Text field state for inputting new saving goal
    var inputGoalText by remember { mutableStateOf(savingGoal.toString()) }

    val savingProgress = (totalBalance / savingGoal).coerceIn(0.0, 1.0)
    val progressColor = when {
        savingProgress >= 1.0 -> Color.Green
        savingProgress >= 0.5 -> Color.Blue
        else -> Color.Red
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
            // Account balance display
            Text("Account Balance:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Text(
                "¥ ${String.format(Locale.US, "%.2f", totalBalance)}",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()

            Spacer(modifier = Modifier.height(8.dp))

            // Income and expense summary
            Text("Income & Expense Summary", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text("Total Income: ¥${String.format(Locale.US, "%.2f", totalIncome)}", color = Color.Green, fontWeight = FontWeight.Bold)
            Text("Total Expense: ¥${String.format(Locale.US, "%.2f", totalExpense)}", color = Color.Red, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()

            Spacer(modifier = Modifier.height(8.dp))

            // Saving goal display
            Text("Saving Goal", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text("Target: ¥${String.format(Locale.US, "%.2f", savingGoal)}", fontWeight = FontWeight.Bold)

            // Progress bar for savings
            LinearProgressIndicator(
                progress = { savingProgress.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = progressColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Input field to set new saving goal
            OutlinedTextField(
                value = inputGoalText,
                onValueChange = { inputGoalText = it },
                label = { Text("Set Saving Goal") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Button to update saving goal
            Button(
                onClick = {
                    inputGoalText.toDoubleOrNull()?.let { newGoal ->
                        viewModel.updateSavingGoal(newGoal)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Adjust Saving Goal")
            }
        }
    }
}

