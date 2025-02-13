package com.example.financetrackerapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val totalBudget = remember { mutableStateOf(5000.00) } // Monthly Budget
    val totalExpense = remember { mutableStateOf(1345.25) } // Total Expenses

    // Compute remaining budget
    val remainingBudget = totalBudget.value - totalExpense.value

    // Dynamic color change based on remaining budget percentage
    val budgetColor = when {
        remainingBudget / totalBudget.value >= 0.5 -> Color.Green  // ≥ 50%
        remainingBudget / totalBudget.value >= 0.2 -> Color.Yellow // 20% - 50%
        else -> Color.Red  // ≤ 20%
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Finance Manager") }) },
        bottomBar = {
            Column {
                // Add Record Banner Button
                Button(
                    onClick = { navController.navigate("add_record") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text("Add Record", style = MaterialTheme.typography.titleLarge)
                }
                // Navigation Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { navController.navigate("statistics") }) {
                        Text("Statistics")
                    }
                    Button(onClick = { navController.navigate("balance") }) {
                        Text("Total Savings")
                    }
                    Button(onClick = { navController.navigate("transactions") }) {
                        Text("Transaction History")
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Budget & Expense Section (Aligned Side by Side)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Monthly Budget", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
                    Text("¥${totalBudget.value}", color = Color.Blue, style = MaterialTheme.typography.bodyLarge)
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Monthly Expense", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
                    Text("¥${totalExpense.value}", color = Color.Red, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Remaining Budget Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Remaining Budget", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
                Text("¥${String.format("%.2f", remainingBudget)}", color = budgetColor, style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Recent Transactions Title
            Text("Recent Transactions", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            // Transaction List
            LazyColumn {
                items(
                    listOf(
                        Triple("Lunch", -15.00, "Cash"),
                        Triple("Taxi", -17.50, "Bank Card"),
                        Triple("Salary", 5000.00, "Bank Card"),
                        Triple("Rent", -1000.00, "Bank Card"),
                        Triple("Lunch", -15.75, "Cash")
                    )
                ) { (category, amount, method) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(category, fontWeight = FontWeight.Bold)
                            Text("¥$amount", color = if (amount >= 0) Color.Green else Color.Red)
                            Text("Payment Method: $method")
                        }
                    }
                }
            }
        }
    }
}
