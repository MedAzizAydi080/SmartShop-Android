package com.example.smartshop.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val quantity: Int,
    val price: Double,
    val firestoreId: String? = null // For cloud synchronization
)
