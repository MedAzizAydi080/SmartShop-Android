package com.example.smartshop.ui.products

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProductViewModel = viewModel()
) {
    val totalCount by viewModel.totalProductCount.collectAsState()
    val totalValue by viewModel.totalStockValue.collectAsState()
    val products by viewModel.allProducts.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistiques", color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Stats Cards
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard("Total Produits", totalCount.toString(), Modifier.weight(1f))
                StatCard("Valeur Stock", "${"%.2f".format(totalValue)} TND", Modifier.weight(1f))
            }

            Text("Répartition de la valeur", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            // Simple Pie Chart
            if (products.isNotEmpty() && totalValue > 0) {
                Box(modifier = Modifier.size(200.dp), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        var startAngle = 0f
                        val colors = listOf(Color(0xFFFF9900), Color(0xFF232F3E), Color(0xFF146EB4), Color(0xFF37475A), Color(0xFFB12704))
                        
                        products.forEachIndexed { index, product ->
                            val sweepAngle = (product.price * product.quantity / totalValue).toFloat() * 360f
                            drawArc(
                                color = colors[index % colors.size],
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = true,
                                size = Size(size.width, size.height)
                            )
                            startAngle += sweepAngle
                        }
                    }
                }
                
                // Legend
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    val colors = listOf(Color(0xFFFF9900), Color(0xFF232F3E), Color(0xFF146EB4), Color(0xFF37475A), Color(0xFFB12704))
                    products.take(5).forEachIndexed { index, product ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(modifier = Modifier.size(12.dp), color = colors[index % colors.size]) {}
                            Spacer(Modifier.width(8.dp))
                            Text("${product.name}: ${"%.1f".format((product.price * product.quantity / totalValue) * 100)}%")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { viewModel.exportToCSV(context) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Exporter en CSV", color = MaterialTheme.colorScheme.onSecondary)
                }
            } else {
                Text("Aucune donnée disponible", color = Color.Gray)
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.tertiary)
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}