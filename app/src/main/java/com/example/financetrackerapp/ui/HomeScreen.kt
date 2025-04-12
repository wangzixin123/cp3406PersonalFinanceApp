package com.example.financetrackerapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.financetrackerapp.viewmodel.TransactionViewModel
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: TransactionViewModel = viewModel()
) {
    val transactions by viewModel.transactions.collectAsState()
    val totalBudget by viewModel.totalBudget
    var newBudgetInput by remember { mutableStateOf("") }

    val totalExpense = transactions.filter { it.amount < 0 }.sumOf { it.amount * -1 }
    val remainingBudget = totalBudget - totalExpense

    val budgetColor = when {
        remainingBudget / totalBudget >= 0.5 -> Color(0xFF2E7D32)
        remainingBudget / totalBudget >= 0.2 -> Color(0xFFF9A825)
        else -> Color(0xFFC62828)
    }

    val recentTransactions = transactions.take(5)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Finance Manager",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { navController.navigate("statistics") },
                    modifier = Modifier.weight(1f)
                ) { Text("Statistics") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { navController.navigate("balance") },
                    modifier = Modifier.weight(1f)
                ) { Text("Savings") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { navController.navigate("transactions") },
                    modifier = Modifier.weight(1f)
                ) { Text("History") }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("add_record") },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                text = { Text("Add Record") },
                modifier = Modifier.padding(bottom = 72.dp)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Budget Summary Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Monthly Budget",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        "¥${"%.2f".format(totalBudget)}",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Monthly Expense",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        "¥${"%.2f".format(totalExpense)}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Remaining Budget Card
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Remaining Budget",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "¥${"%.2f".format(remainingBudget)}",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = budgetColor
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Budget Controls
            Column {
                Text(
                    "Adjust Budget",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = newBudgetInput,
                        onValueChange = { newBudgetInput = it },
                        label = { Text("Amount") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            newBudgetInput.toDoubleOrNull()?.let {
                                viewModel.updateTotalBudget(it)
                                newBudgetInput = ""
                            }
                        },
                        modifier = Modifier.height(56.dp)
                    ) {
                        Text("Update")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recent Transactions
            Text(
                "Recent Transactions",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (recentTransactions.isEmpty()) {
                Text(
                    "No transactions yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(recentTransactions) { transaction ->
                            TransactionCard(
                                transaction = transaction,
                                showDeleteButton = false
                            )
                        }
                    }
                }
            }
        }
    }
}