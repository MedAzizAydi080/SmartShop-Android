package com.example.smartshop.ui.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartshop.data.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onAddProduct: () -> Unit,
    onEditProduct: (Product) -> Unit,
    onViewStats: () -> Unit,
    viewModel: ProductViewModel = viewModel()
) {
    val products by viewModel.allProducts.collectAsState()
    val totalValue by viewModel.totalStockValue.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventaire SmartShop", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onViewStats) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Stats")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddProduct,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Stats Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Valeur totale du stock", style = MaterialTheme.typography.labelMedium)
                    Text("${"%.2f".format(totalValue)} TND", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(products) { product ->
                    ProductItem(
                        product = product,
                        onEdit = { onEditProduct(product) },
                        onDelete = { viewModel.deleteProduct(product) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Quantité: ${product.quantity}", style = MaterialTheme.typography.bodyMedium)
                Text("${product.price} TND", style = MaterialTheme.typography.titleMedium, color = Color(0xFFB12704), fontWeight = FontWeight.Bold)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Modifier", tint = MaterialTheme.colorScheme.tertiary)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
