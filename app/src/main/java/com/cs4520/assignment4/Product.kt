package com.cs4520.assignment4

// Sealed classes for product list (from dataset) in the ProductAdapter

sealed class Product {
    abstract val name: String
    abstract val price: Int
    abstract val expiryDate: String?

    data class Food(
        override val name: String,
        override val expiryDate: String?,
        override val price: Int
    ) : Product()

    data class Equipment(
        override val name: String,
        override val expiryDate: String?,
        override val price: Int
    ) : Product()
}

