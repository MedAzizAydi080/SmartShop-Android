package com.example.smartshop.data.repository

import com.example.smartshop.data.local.ProductDao
import com.example.smartshop.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ProductRepository(
    private val productDao: ProductDao,
    private val firestore: FirebaseFirestore
) {
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    suspend fun insert(product: Product) {
        // 1. Generate ID locally
        val docRef = firestore.collection("products").document()
        val firestoreId = docRef.id

        // 2. Save to Local Room WITH firestoreId
        val productWithFirestoreId = product.copy(firestoreId = firestoreId)
        val id = productDao.insertProduct(productWithFirestoreId)
        
        // 3. Sync to Firestore
        try {
            val productFinal = productWithFirestoreId.copy(id = id.toInt())
            docRef.set(productFinal).await()
        } catch (e: Exception) {
            // Handle error (e.g., log or retry later)
        }
    }

    suspend fun update(product: Product) {
        productDao.updateProduct(product)
        product.firestoreId?.let { firestoreId ->
            try {
                firestore.collection("products").document(firestoreId).set(product).await()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    suspend fun delete(product: Product) {
        productDao.deleteProduct(product)
        product.firestoreId?.let { firestoreId ->
            try {
                firestore.collection("products").document(firestoreId).delete().await()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    // Real-time sync from Firestore to Room
    fun syncFromFirestore(): Flow<Unit> = callbackFlow {
        val registration = firestore.collection("products")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                
                snapshot?.let { querySnapshot ->
                    for (change in querySnapshot.documentChanges) {
                        val doc = change.document
                        
                        // Ignore local changes (latency compensation) to prevent duplication/loops
                        if (doc.metadata.hasPendingWrites()) continue

                        val firestoreId = doc.id
                        // Parse safely
                        val name = doc.getString("name") ?: ""
                        val quantity = doc.getLong("quantity")?.toInt() ?: 0
                        val price = doc.getDouble("price") ?: 0.0
                        
                        // We do NOT trust the 'id' from firestore as it might be from another device's Room DB.
                        // We rely on 'firestoreId' for mapping.
                        
                        launch {
                            val localProduct = productDao.getProductByFirestoreId(firestoreId)

                            when (change.type) {
                                com.google.firebase.firestore.DocumentChange.Type.ADDED,
                                com.google.firebase.firestore.DocumentChange.Type.MODIFIED -> {
                                    val productToSave = if (localProduct != null) {
                                        // Update existing, preserving local ID
                                        localProduct.copy(
                                            name = name,
                                            quantity = quantity,
                                            price = price,
                                            firestoreId = firestoreId
                                        )
                                    } else {
                                        // Insert new
                                        Product(
                                            name = name,
                                            quantity = quantity,
                                            price = price,
                                            firestoreId = firestoreId
                                        )
                                    }
                                    
                                    if (localProduct != null) {
                                        productDao.updateProduct(productToSave)
                                    } else {
                                        productDao.insertProduct(productToSave)
                                    }
                                }
                                com.google.firebase.firestore.DocumentChange.Type.REMOVED -> {
                                    localProduct?.let { productDao.deleteProduct(it) }
                                }
                            }
                        }
                    }
                }
                trySend(Unit)
            }
        awaitClose { registration.remove() }
    }
}
