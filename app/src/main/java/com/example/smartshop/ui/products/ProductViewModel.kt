package com.example.smartshop.ui.products

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartshop.data.local.AppDatabase
import com.example.smartshop.data.model.Product
import com.example.smartshop.data.repository.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProductRepository
    val allProducts: StateFlow<List<Product>>

    // Statistics flows
    val totalProductCount: StateFlow<Int>
    val totalStockValue: StateFlow<Double>

    init {
        val productDao = AppDatabase.getDatabase(application).productDao()
        repository = ProductRepository(productDao, FirebaseFirestore.getInstance())
        
        allProducts = repository.allProducts.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        totalProductCount = allProducts.map { it.size }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

        totalStockValue = allProducts.map { products ->
            products.sumOf { it.price * it.quantity }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

        // Start Cloud Sync
        viewModelScope.launch {
            repository.syncFromFirestore().collect {
                // Sync active
            }
        }
    }

    fun addProduct(name: String, quantity: Int, price: Double) {
        viewModelScope.launch {
            repository.insert(Product(name = name, quantity = quantity, price = price))
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.update(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.delete(product)
        }
    }

    fun exportToCSV(context: Context) {
        val products = allProducts.value
        if (products.isEmpty()) {
            Toast.makeText(context, "Aucun produit à exporter", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val fileName = "smartshop_inventory.csv"
            val fileContents = StringBuilder()
            fileContents.append("ID,Nom,Quantité,Prix (TND)\n")
            products.forEach { 
                fileContents.append("${it.id},${it.name},${it.quantity},${it.price}\n")
            }

            val file = File(context.getExternalFilesDir(null), fileName)
            FileOutputStream(file).use {
                it.write(fileContents.toString().toByteArray())
            }
            Toast.makeText(context, "Exporté vers: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Erreur d'export: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}