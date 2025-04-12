package com.example.financetrackerapp.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.financetrackerapp.viewmodel.TransactionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavController,
    viewModel: TransactionViewModel = viewModel()
) {
    val transactions by viewModel.transactions.collectAsState()

    val now = LocalDate.now()
    var selectedTab by remember { mutableStateOf(0) } // 0 = Monthly, 1 = Yearly
    var selectedYear by remember { mutableIntStateOf(now.year) }
    var selectedMonth by remember { mutableIntStateOf(now.monthValue) }
    var selectedType by remember { mutableStateOf("Expense") }

    val isExpense = selectedType == "Expense"

    // Filter transactions by selected year, month, and type
    val filteredTransactions = transactions.filter {
        it.date.year == selectedYear &&
                (selectedTab == 1 || it.date.monthValue == selectedMonth) &&
                if (isExpense) it.amount < 0 else it.amount > 0
    }

    val totalAmount = filteredTransactions.sumOf { kotlin.math.abs(it.amount) }

    // Top categories by amount
    val categoryTotals = filteredTransactions
        .groupBy { it.category }
        .mapValues { (_, list) -> list.sumOf { kotlin.math.abs(it.amount) } }
        .toList()
        .sortedByDescending { it.second }
        .take(5)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistics") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Top tab: Monthly / Yearly
            TabRow(selectedTabIndex = selectedTab) {
                listOf("Monthly", "Yearly").forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Time switcher and type switcher
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        if (selectedTab == 0) {
                            if (selectedMonth == 1) {
                                selectedMonth = 12
                                selectedYear--
                            } else {
                                selectedMonth--
                            }
                        } else {
                            selectedYear--
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
                    }

                    Text(
                        if (selectedTab == 0)
                            "$selectedYear-${selectedMonth.toString().padStart(2, '0')}"
                        else
                            "$selectedYear",
                        style = MaterialTheme.typography.titleMedium
                    )

                    IconButton(onClick = {
                        if (selectedTab == 0) {
                            if (selectedMonth == 12) {
                                selectedMonth = 1
                                selectedYear++
                            } else {
                                selectedMonth++
                            }
                        } else {
                            selectedYear++
                        }
                    }) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                    }
                }

                Row {
                    listOf("Expense", "Income").forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text(type) },
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Total amount display
            Text(
                "¥ ${"%.2f".format(totalAmount)}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = if (isExpense) Color.Red else Color(0xFF2E7D32)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bar chart section
            Text("${selectedType} Comparison (Bar Chart)", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(horizontal = 12.dp)
            ) {
                val barWidth = size.width / 12f * 0.6f
                val spacing = size.width / 12f
                val maxValue = (1..12).maxOf { month ->
                    transactions.filter {
                        (if (isExpense) it.amount < 0 else it.amount > 0) &&
                                it.date.year == selectedYear &&
                                it.date.monthValue == month
                    }.sumOf { kotlin.math.abs(it.amount) }
                }.coerceAtLeast(1.0)

                for (month in 1..12) {
                    val total = transactions.filter {
                        (if (isExpense) it.amount < 0 else it.amount > 0) &&
                                it.date.year == selectedYear &&
                                it.date.monthValue == month
                    }.sumOf { kotlin.math.abs(it.amount) }

                    val heightRatio = total / maxValue
                    val barHeight = (size.height - 30.dp.toPx()) * heightRatio.toFloat()

                    val x = (month - 1) * spacing + (spacing - barWidth) / 2
                    val y = size.height - barHeight

                    drawRect(
                        color = if (month == selectedMonth) Color(0xFF4CAF50) else Color.LightGray,
                        topLeft = androidx.compose.ui.geometry.Offset(x, y),
                        size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
                    )

                    drawIntoCanvas {
                        it.nativeCanvas.drawText(
                            "${month}M",
                            x + barWidth / 2,
                            size.height,
                            android.graphics.Paint().apply {
                                textSize = 26f
                                color = android.graphics.Color.GRAY
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Top categories ranking
            Text("${selectedType} Ranking", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            if (categoryTotals.isEmpty()) {
                Text("No Data Available")
            } else {
                categoryTotals.forEachIndexed { index, (category, amount) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${index + 1}. $category")
                        Text("¥ ${"%.2f".format(amount)}")
                    }
                }
            }
        }
    }
}
