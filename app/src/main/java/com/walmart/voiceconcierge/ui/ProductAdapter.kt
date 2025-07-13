package com.walmart.voiceconcierge.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.walmart.voiceconcierge.Product
import com.walmart.voiceconcierge.databinding.ItemProductBinding
import com.walmart.voiceconcierge.db.*
import kotlinx.coroutines.*

class ProductAdapter(
    private val products: List<Product>,
    private val db: AppDatabase,
    private val stateCode: String?
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        val b = holder.binding


        b.tvTitle.text = product.title
        b.tvAisle.text = "Aisle: ${product.aisle ?: "N/A"}"
        b.tvPrice.text = "₹${String.format("%.2f", product.price)} • ${product.likes} likes • ${product.cartAdds} carts"

        stateCode?.let { state ->
            val localSales = product.regionSales[state] ?: 0
            if (localSales > 0) {
                b.tvAisle.append(" • Popular in $state")
            }
        }


        Glide.with(b.root)
            .load(product.image)
            .into(b.ivProduct)


        b.btnAddToCart.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.cartDao().insert(
                    CartItem(
                        productId = product.id.toString(),
                        productName = product.title,
                        aisle = product.aisle ?: "General",
                        quantity = 1,
                        price = product.price
                    )
                )
            }
        }


        b.btnLike.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val productId = product.id.toString()
                db.feedbackDao().insertOrUpdate(
                    Feedback(productId = productId, score = 1)
                )
                stateCode?.let { code ->
                    db.stateLogDao().insertOrUpdate(
                        StateLog(
                            productId = productId,
                            stateCode = code,
                            score = 1
                        )
                    )
                }
            }
        }


        b.btnDislike.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val productId = product.id.toString()
                db.feedbackDao().insertOrUpdate(
                    Feedback(productId = productId, score = -1)
                )
                stateCode?.let { code ->
                    db.stateLogDao().insertOrUpdate(
                        StateLog(
                            productId = productId,
                            stateCode = code,
                            score = -1
                        )
                    )
                }
            }
        }
    }
}
