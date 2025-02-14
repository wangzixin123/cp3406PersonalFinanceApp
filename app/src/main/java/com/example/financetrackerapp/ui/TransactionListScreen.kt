package com.example.financetrackerapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.IconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(navController: NavController) {
    var filter by remember { mutableStateOf("All") } // Stores the selected filter option

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
            // Transaction filter buttons
            Row {
                Button(onClick = { filter = "Day" }) { Text("Day") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { filter = "Week" }) { Text("Week") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { filter = "Month" }) { Text("Month") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { filter = "All" }) { Text("All") }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Current Filter: $filter", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            // Transaction list
            LazyColumn {
                items(
                    listOf(
                        "2025-02-01 | Lunch | -¥15.00 | Cash",
                        "2025-02-02 | Transport | -¥20.00 | Cash",
                        "2025-02-03 | Entertainment | -¥50.00 | Cash",
                        "2025-02-05 | Salary | +¥5000.00 | Bank Card"
                    )
                ) { transaction ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Text(transaction, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}


