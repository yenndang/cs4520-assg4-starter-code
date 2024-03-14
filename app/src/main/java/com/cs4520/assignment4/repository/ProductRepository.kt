package com.cs4520.assignment4.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.cs4520.assignment4.NetworkUtils
import com.cs4520.assignment4.api.RetrofitInstance
import com.cs4520.assignment4.models.Product
import com.cs4520.assignment4.models.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProductRepository(private val productDao: ProductDao, private val context: Context) {

    private val allProducts: LiveData<List<ProductEntity>> = productDao.getAllProducts()

    suspend fun getProducts(page: Int? = null): LiveData<List<Product>> = withContext(Dispatchers.IO) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            val response = RetrofitInstance.apiService.getProducts(page)
            if (response.isSuccessful) {
                response.body()?.let { networkProducts ->
                    val productsToCache = networkProducts.map {
                        ProductEntity(
                            name = it.name,
                            price = it.price,
                            expiryDate = it.expiryDate,
                            type = it.type
                        )
                    }
                    productDao.insertAll(productsToCache)
                    Log.d("ProductRepository", "Saving ${productsToCache.size} products")
                }
            }
            MutableLiveData(response.body() ?: emptyList())
        } else {
            // Transform LiveData<List<ProductEntity>> to LiveData<List<Product>>
            Transformations.map(allProducts) { entities ->
                entities.map { entity ->
                    Product(
                        name = entity.name,
                        price = entity.price,
                        expiryDate = entity.expiryDate,
                        type = entity.type
                    )
                }
            }
        }
    }
}
