//package com.cs4520.assignment1
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.cs4520.assignment4.ui.ProductAdapter
//import com.cs4520.assignment4.databinding.FragmentProductListBinding
//
//class ProductListFragment : Fragment() {
//
//    private var _binding: FragmentProductListBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var productAdapter: ProductAdapter
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        _binding = FragmentProductListBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setupRecyclerView()
//    }
//
//    private fun setupRecyclerView() {
//        binding.recyclerView.layoutManager = LinearLayoutManager(context)
//        binding.recyclerView.adapter = ProductAdapter(emptyList())
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}
package com.cs4520.assignment4.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs4520.assignment4.databinding.FragmentProductListBinding
import com.cs4520.assignment4.repository.ProductRepository
import com.cs4520.assignment4.ui.viewmodel.ProductViewModel
import com.cs4520.assignment4.ui.viewmodel.ProductViewModelFactory

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    // Initialize ProductAdapter with an empty list
    private val productAdapter by lazy { ProductAdapter(emptyList()) }

    // Use the 'by viewModels()' Kotlin property delegate for ViewModel initialization
    private val productViewModel: ProductViewModel by viewModels {
        ProductViewModelFactory(ProductRepository())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productAdapter
        }
    }

    private fun observeViewModel() {
        productViewModel.products.observe(viewLifecycleOwner) { products ->
            // Update the RecyclerView adapter's dataset
            productAdapter.updateProducts(products)
        }

        productViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Show or hide the ProgressBar based on the loading state
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Trigger loading products from the ViewModel
        productViewModel.getProducts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
