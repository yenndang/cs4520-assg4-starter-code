package com.cs4520.assignment4.repository

import androidx.lifecycle.LiveData
import com.cs4520.assignment4.api.RetrofitInstance
import com.cs4520.assignment4.models.Product
import com.cs4520.assignment4.models.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProductRepository(private val productDao: ProductDao) {

    // LiveData for observing the product list from the database
    val allProducts: LiveData<List<ProductEntity>> = productDao.getAllProducts()


    suspend fun getProducts(page: Int? = null): Response<List<Product>> = withContext(Dispatchers.IO) {
        val response = RetrofitInstance.apiService.getProducts(page)
        if (response.isSuccessful) {
            // Convert the network model to the database entity and insert
            response.body()?.let { networkProducts ->
                val productsToCache = networkProducts.map {
                    ProductEntity(
                        name = it.name,
                        price = it.price,
                        expiryDate = it.expiryDate,
                        type = it.type
                    )
                }
                // Clear old data
                productDao.deleteAllProducts()
                // Cache new data
                productDao.insertAll(productsToCache)
            }
        }
        response
    }

    // Method to fetch products from the database
    fun getCachedProducts(): LiveData<List<ProductEntity>> {
        return allProducts
    }

    // Helper function to clear cached products
    suspend fun clearCachedProducts() {
        withContext(Dispatchers.IO) {
            productDao.deleteAllProducts()
        }
    }

}
