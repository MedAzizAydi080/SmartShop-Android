package com.example.smartshop.ui.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartshop.data.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEntryScreen(
    productToEdit: Product? = null,
    onNavigateBack: () -> Unit,
    viewModel: ProductViewModel = viewModel()
) {
    var name by remember { mutableStateOf(productToEdit?.name ?: "") }
    var quantity by remember { mutableStateOf(productToEdit?.quantity?.toString() ?: "") }
    var price by remember { mutableStateOf(productToEdit?.price?.toString() ?: "") }
    
    var nameError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (productToEdit == null) "Ajouter un produit" else "Modifier le produit", color = MaterialTheme.colorScheme.onPrimary) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; nameError = false },
                label = { Text("Nom du produit") },
                modifier = Modifier.fillMaxWidth(),
                isError = nameError,
                supportingText = { if (nameError) Text("Le nom est obligatoire") }
            )

            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it; quantityError = false },
                label = { Text("Quantité") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = quantityError,
                supportingText = { if (quantityError) Text("Quantité invalide (doit être ≥ 0)") }
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it; priceError = false },
                label = { Text("Prix (TND)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = priceError,
                supportingText = { if (priceError) Text("Prix invalide (doit être > 0)") }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val q = quantity.toIntOrNull()
                    val p = price.toDoubleOrNull()
                    
                    if (name.isBlank()) nameError = true
                    if (q == null || q < 0) quantityError = true
                    if (p == null || p <= 0) priceError = true
                    
                    if (!nameError && !quantityError && !priceError) {
                        if (productToEdit == null) {
                            viewModel.addProduct(name, q!!, p!!)
                        } else {
                            viewModel.updateProduct(productToEdit.copy(name = name, quantity = q!!, price = p!!))
                        }
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(if (productToEdit == null) "Enregistrer" else "Mettre à jour", color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    }
}
