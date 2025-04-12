package com.example.financetrackerapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.financetrackerapp.data.Transaction
import com.example.financetrackerapp.viewmodel.TransactionViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    navController: NavController,
    viewModel: TransactionViewModel = viewModel()
) {
    val transactions by viewModel.transactions.collectAsState()
    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transaction History") },
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
                .padding(16.dp)
        ) {
            if (transactions.isEmpty()) {
                Text("No transactions yet.", fontWeight = FontWeight.Bold)
            } else {
                LazyColumn {
                    items(transactions) { transaction ->
                        TransactionCard(
                            transaction = transaction,
                            showDeleteButton = true,
                            onDelete = { transactionToDelete = it }
                        )
                    }
                }
            }
        }
    }

    // Confirmation dialog for deleting a transaction
    if (transactionToDelete != null) {
        AlertDialog(
            onDismissRequest = { transactionToDelete = null },
            title = { Text("Delete Confirmation") },
            text = { Text("Are you sure you want to delete this transaction? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteTransaction(transactionToDelete!!)
                    transactionToDelete = null
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { transactionToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun TransactionCard(
    transaction: Transaction,
    showDeleteButton: Boolean = true,
    onDelete: (Transaction) -> Unit = {}
) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val amountColor = if (transaction.amount >= 0) Color(0xFF2E7D32) else Color.Red

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Date: ${transaction.date.format(formatter)}", fontWeight = FontWeight.Bold)
                Text("Category: ${transaction.category}")
                Text("Amount: ¥${"%.2f".format(transaction.amount)}", color = amountColor)
                Text("Payment: ${transaction.paymentMethod}")
            }

            if (showDeleteButton) {
                IconButton(onClick = { onDelete(transaction) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}
