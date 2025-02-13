package com.example.financetrackerapp.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.IconButton
import java.util.Locale
import android.graphics.Paint
import android.graphics.Typeface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(navController: NavController) {
    var selectedYear by remember { mutableIntStateOf(2025) }
    var selectedPieChartRange by remember { mutableStateOf("This Month") }
    var expanded by remember { mutableStateOf(false) }  // Controls dropdown menu state

    val monthlyData = listOf(1200f, 950f, 1100f, 1300f, 900f, 850f, 1250f, 1400f, 1500f, 1350f, 1200f, 1100f)

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
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Expense data range selection
            Text("Select Expense Data Range", fontWeight = FontWeight.Bold)

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(onClick = { expanded = true }) {
                    Text(selectedPieChartRange)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf("Today", "Last Week", "This Month", "This Year").forEach { range ->
                        DropdownMenuItem(
                            text = { Text(range) },
                            onClick = {
                                selectedPieChartRange = range
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Expense Category Distribution ($selectedPieChartRange)", fontWeight = FontWeight.Bold)

            // Expense pie chart
            PieChart(selectedPieChartRange)

            // Legend for pie chart
            PieChartLegend()

            Spacer(modifier = Modifier.height(24.dp))
            Text("Spending Trend", fontWeight = FontWeight.Bold)

            // Year selection for spending trend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { selectedYear-- }) { Text("<") }
                Spacer(modifier = Modifier.width(16.dp))
                Text(selectedYear.toString(), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { selectedYear++ }) { Text(">") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Line chart showing monthly spending
            LineChart(data = monthlyData)
        }
    }
}

// Pie chart component displaying expense distribution
@Composable
fun PieChart(range: String) {
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow)
    val angles = when (range) {
        "Today" -> listOf(30f, 60f, 90f, 180f)
        "Last Week" -> listOf(80f, 70f, 100f, 110f)
        "This Month" -> listOf(120f, 90f, 80f, 70f)
        "This Year" -> listOf(140f, 60f, 100f, 60f)
        else -> listOf(120f, 90f, 80f, 70f)
    }

    Canvas(
        modifier = Modifier
            .size(200.dp)
            .padding(16.dp)
    ) {
        var startAngle = 0f
        for (i in angles.indices) {
            drawArc(
                color = colors[i],
                startAngle = startAngle,
                sweepAngle = angles[i],
                useCenter = true
            )
            startAngle += angles[i]
        }
    }
}

// Legend component for pie chart categories
@Composable
fun PieChartLegend() {
    val pieData = listOf(
        Pair("Food", Color.Red),
        Pair("Transport", Color.Green),
        Pair("Entertainment", Color.Blue),
        Pair("Other", Color.Yellow)
    )

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        pieData.forEach { (category, color) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Canvas(modifier = Modifier.size(12.dp)) {
                    drawCircle(color)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(category, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

// Line chart component displaying monthly spending trend
@Composable
fun LineChart(data: List<Float>) {
    val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(16.dp)
    ) {
        val maxY = data.maxOrNull() ?: 1f
        val minY = data.minOrNull() ?: 0f
        val rangeY = maxY - minY
        val stepX = size.width / (data.size - 1)

        // Create a path for the line chart
        val path = Path().apply {
            moveTo(0f, size.height - ((data[0] - minY) / rangeY * size.height))
            for (i in 1 until data.size) {
                lineTo(i * stepX, size.height - ((data[i] - minY) / rangeY * size.height))
            }
        }

        // Draw text labels for months
        drawIntoCanvas { canvas ->
            val textPaint = Paint().apply {
                textSize = 30f
                color = android.graphics.Color.BLACK
                textAlign = Paint.Align.CENTER
                typeface = Typeface.DEFAULT_BOLD
            }

            months.forEachIndexed { i, month ->
                val x = i * stepX
                canvas.nativeCanvas.drawText(month, x, size.height + 30f, textPaint)
            }
        }

        // Draw the line chart
        drawPath(path, color = Color.Blue, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 5f))

        // Draw data points on the chart
        for (i in data.indices) {
            val x = i * stepX
            val y = size.height - ((data[i] - minY) / rangeY * size.height)
            drawCircle(Color.Red, radius = 5f, center = Offset(x, y))
        }
    }
}
