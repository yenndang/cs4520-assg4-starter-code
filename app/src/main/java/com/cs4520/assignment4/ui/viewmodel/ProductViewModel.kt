package com.cs4520.assignment4.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs4520.assignment4.models.Product
import com.cs4520.assignment4.models.ProductEntity
import com.cs4520.assignment4.repository.ProductRepository
import com.cs4520.assignment4.utils.NetworkUtils
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val cachedProducts: LiveData<List<ProductEntity>> = repository.getCachedProducts()


    fun getProducts(context: Context, page: Int? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            if (NetworkUtils.isNetworkAvailable(context)) {
                // Existing network request logic
                try {
                    val response = repository.getProducts(page)
                    if (response.isSuccessful && response.body() != null) {
                        _products.postValue(response.body())
                    } else {
                        _products.postValue(emptyList())
                    }
                } finally {
                    _isLoading.postValue(false)
                }
            } else {
                // Convert ProductEntity to Product before posting
                val cachedProducts = repository.getCachedProducts().value
                val productModelList = cachedProducts?.map { entity ->
                    Product(
                        name = entity.name,
                        price = entity.price,
                        expiryDate = entity.expiryDate,
                        type = entity.type
                    )
                } ?: emptyList()

                _products.postValue(productModelList)
            }
            _isLoading.value = false
        }
    }

}
