package com.cs4520.assignment4.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val name: String,
    val price: Double,
    val expiryDate: String?,
    val type: String
)
