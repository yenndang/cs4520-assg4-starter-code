package com.cs4520.assignment4.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cs4520.assignment4.models.Product
import com.cs4520.assignment4.R

class ProductAdapter(private var products: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun getItemCount(): Int = products.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productImage: ImageView = itemView.findViewById(R.id.imageViewProductIcon)
        private val productName: TextView = itemView.findViewById(R.id.textViewName)
        private val productPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        private val productExpiryDate: TextView = itemView.findViewById(R.id.textViewExpiryDate)

        fun bind(product: Product) {
            productName.text = product.name
            productPrice.text = "$${product.price}"

            // Handle expiry date visibility and text
            productExpiryDate.apply {
                if (product.expiryDate != null) {
                    visibility = View.VISIBLE
                    text = product.expiryDate
                } else {
                    visibility = View.GONE
                }
            }

            // Update this block to handle product type
            when (product.type) {
                "Food" -> {
                    itemView.setBackgroundColor(Color.parseColor("#FFD965")) // Light Yellow
                    productImage.setImageResource(R.drawable.food)
                }
                "Equipment" -> {
                    itemView.setBackgroundColor(Color.parseColor("#E06666")) // Light Red
                    productImage.setImageResource(R.drawable.equipment)
                }
                else -> { // Optional: handle unexpected type, if necessary
                    itemView.setBackgroundColor(Color.parseColor("#FFFFFF")) // Default or error color
                }
            }

            // Setting text color for all TextViews inside the item
            listOf(productName, productPrice, productExpiryDate).forEach { textView ->
                textView.setTextColor(Color.parseColor("#000000"))
            }
        }
    }
}
