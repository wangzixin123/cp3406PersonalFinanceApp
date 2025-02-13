package com.example.financetrackerapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.IconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordScreen(navController: NavController) {
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Food") } // Default category
    var paymentMethod by remember { mutableStateOf("Cash") } // Default payment method

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Record") },
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
            verticalArrangement = Arrangement.Top
        ) {
            // Input field for amount
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category selection dropdown
            Text("Category")
            DropdownMenuExample(
                listOf("Food", "Transport", "Entertainment", "Salary"),
                selectedItem = category,
                onItemSelected = { category = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Payment method selection dropdown
            Text("Payment Method")
            DropdownMenuExample(
                listOf("Cash", "Bank Card", "Other"),
                selectedItem = paymentMethod,
                onItemSelected = { paymentMethod = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save and cancel buttons
            Row {
                Button(onClick = { /* TODO: Implement save logic */ }) {
                    Text("Confirm")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { navController.popBackStack() }) {
                    Text("Cancel")
                }
            }
        }
    }
}

// Dropdown menu component
@Composable
fun DropdownMenuExample(items: List<String>, selectedItem: String, onItemSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(selectedItem)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

