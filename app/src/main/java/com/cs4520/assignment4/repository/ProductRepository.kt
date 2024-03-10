package com.cs4520.assignment4.repository

import com.cs4520.assignment4.api.RetrofitInstance
import com.cs4520.assignment4.models.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

// Makes a network request to fetch products. It uses Kotlin Coroutines to perform this operation
// in a background thread by switching the context to Dispatchers.IO.
class ProductRepository {

    suspend fun getProducts(page: Int? = null): Response<List<Product>> = withContext(Dispatchers.IO) {
        RetrofitInstance.apiService.getProducts(page)
    }
}
