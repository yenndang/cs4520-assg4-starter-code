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
    val products: LiveData<List<Product>> get() = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getProducts(page: Int? = null) {
        _isLoading.value = true
        viewModelScope.launch {
            val productLiveData = repository.getProducts(page)
            productLiveData.observeForever { productList ->
                _products.postValue(productList)
                _isLoading.postValue(false)
            }
        }
    }

}

