package com.cs4520.assignment4.models

data class Product(
    val name: String,
    val price: Double,
    val expiryDate: String?, // Use null for optional field
    val type: String
)
