package com.cs4520.assignment4.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs4520.assignment4.models.Product
import com.cs4520.assignment4.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getProducts(page: Int? = null) {
        viewModelScope.launch {
            _isLoading.value = true
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
        }
    }


}
