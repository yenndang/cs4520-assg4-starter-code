package com.cs4520.assignment4.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs4520.assignment4.models.Product
import com.cs4520.assignment4.models.ProductEntity
import com.cs4520.assignment4.repository.ProductRepository
import com.cs4520.assignment4.NetworkUtils
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val cachedProductsObserver = Observer<List<ProductEntity>> { cachedProducts ->
        val productModelList = cachedProducts.map { entity ->
            Product(
                name = entity.name,
                price = entity.price,
                expiryDate = entity.expiryDate,
                type = entity.type
            )
        }
        _products.postValue(productModelList)
    }

    fun getProducts(context: Context, page: Int? = null) {
        _isLoading.value = true
        if (NetworkUtils.isNetworkAvailable(context)) {
            viewModelScope.launch {
                try {
                    val response = repository.getProducts(page)
                    if (response.isSuccessful && response.body() != null) {
                        _products.postValue(response.body())
                    } else {
                        _products.postValue(emptyList())
                    }
                } catch (e: Exception) {
                    _products.postValue(emptyList())
                } finally {
                    _isLoading.postValue(false)
                }
            }
        } else {
            viewModelScope.launch {
                repository.getCachedProducts().observeForever(cachedProductsObserver)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Clean up the observer to prevent memory leaks
        repository.getCachedProducts().removeObserver(cachedProductsObserver)
    }
}


