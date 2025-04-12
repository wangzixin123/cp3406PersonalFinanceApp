package com.example.financetrackerapp.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financetrackerapp.data.Transaction
import com.example.financetrackerapp.viewmodel.TransactionViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordScreen(
    navController: NavController,
    viewModel: TransactionViewModel = viewModel()
) {
    val context = LocalContext.current

    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Food") }
    var customCategory by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("Cash") }
    var customPaymentMethod by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

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
            // Amount input field
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category dropdown
            Text("Category")
            DropdownMenuExample(
                items = listOf("Food", "Transport", "Entertainment", "Salary", "Other"),
                selectedItem = category,
                onItemSelected = { category = it }
            )

            if (category == "Other") {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = customCategory,
                    onValueChange = { customCategory = it },
                    label = { Text("Enter Custom Category") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Payment method dropdown
            Text("Payment Method")
            DropdownMenuExample(
                items = listOf("Cash", "Bank Card", "Other"),
                selectedItem = paymentMethod,
                onItemSelected = { paymentMethod = it }
            )

            if (paymentMethod == "Other") {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = customPaymentMethod,
                    onValueChange = { customPaymentMethod = it },
                    label = { Text("Enter Custom Payment Method") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date picker
            Text("Date")
            Button(
                onClick = {
                    val calendar = Calendar.getInstance()
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                        },
                        selectedDate.year,
                        selectedDate.monthValue - 1,
                        selectedDate.dayOfMonth
                    ).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedDate.format(dateFormatter))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Confirm and cancel buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    val parsedAmount = amount.toDoubleOrNull()
                    if (parsedAmount != null) {
                        val signedAmount = if (category == "Salary") abs(parsedAmount) else -abs(parsedAmount)

                        val finalCategory = if (category == "Other" && customCategory.isNotBlank()) {
                            customCategory
                        } else {
                            category
                        }

                        val finalPaymentMethod = if (paymentMethod == "Other" && customPaymentMethod.isNotBlank()) {
                            customPaymentMethod
                        } else {
                            paymentMethod
                        }

                        val transaction = Transaction(
                            amount = signedAmount,
                            category = finalCategory,
                            paymentMethod = finalPaymentMethod,
                            date = selectedDate
                        )

                        viewModel.insertTransaction(transaction)
                        navController.popBackStack()
                    }
                }) {
                    Text("Confirm")
                }

                Button(onClick = { navController.popBackStack() }) {
                    Text("Cancel")
                }
            }
        }
    }
}

// Reusable dropdown menu component
@Composable
fun DropdownMenuExample(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
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
