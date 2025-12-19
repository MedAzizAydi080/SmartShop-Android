package com.example.smartshop.data.local

import androidx.room.*
import com.example.smartshop.data.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Int): Product?

    @Query("SELECT * FROM products WHERE firestoreId = :firestoreId")
    suspend fun getProductByFirestoreId(firestoreId: String): Product?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)
    
    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()
}
