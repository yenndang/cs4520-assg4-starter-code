package com.cs4520.assignment4.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs4520.assignment4.api.AppDatabaseSingleton
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
        // Get the ProductDao from the Room database instance, which requires a Context
        val productDao = AppDatabaseSingleton.getDatabase(requireContext()).productDao()
        // Pass both productDao and context to the ProductRepository
        val productRepository = ProductRepository(productDao, requireContext())
        // Create the ViewModelFactory with the repository
        ProductViewModelFactory(productRepository)
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
            Log.d("ProductListFragment", "Observing ${products.size} products")
            if (products.isNullOrEmpty()) {
                binding.textViewEmpty.visibility = View.VISIBLE // Show "No products available"
                binding.recyclerView.visibility = View.GONE // Hide RecyclerView
            } else {
                binding.textViewEmpty.visibility = View.GONE // Hide "No products available"
                binding.recyclerView.visibility = View.VISIBLE // Show RecyclerView
                productAdapter.updateProducts(products) // Update the RecyclerView adapter's dataset
            }
            binding.progressBar.visibility = View.GONE
        }

        productViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.textViewEmpty.visibility = View.GONE // Optionally hide the empty view when loading starts
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        // Trigger loading products from the ViewModel
        productViewModel.getProducts(1)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
