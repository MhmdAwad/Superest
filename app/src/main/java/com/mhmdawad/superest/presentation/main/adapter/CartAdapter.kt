package com.mhmdawad.superest.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mhmdawad.superest.BR
import com.mhmdawad.superest.R
import com.mhmdawad.superest.model.ProductModel
import com.mhmdawad.superest.util.ADVANCED_SHOP_LAYOUT
import com.mhmdawad.superest.util.SIMPLE_SHOP_LAYOUT
import javax.inject.Inject

class CartAdapter
@Inject
constructor() : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val productsList = mutableListOf<ProductModel>()
    private val purchasedProductsList = mutableListOf<ProductModel>()
    private lateinit var productListener: ProductListener

    fun addProducts(list: List<ProductModel>, listener: ProductListener) {
        productListener = listener
        productsList.clear()
        purchasedProductsList.clear()
        productsList.addAll(list)
        purchasedProductsList.addAll(list)
        notifyDataSetChanged()
    }

    fun getPurchasedProducts() = purchasedProductsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.cart_item_rv_layout, parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(productsList[position])
    }

    override fun getItemCount(): Int = productsList.size

    fun onQuantityTextChanged(
        text: CharSequence,
        productModel: ProductModel,
        priceTextView: TextView
    ) {
        val quantity = text.toString()
        if (quantity.isNotEmpty()) {
            val quantityNumber = quantity.toDouble().toInt()
            if (quantityNumber > 0) {
                val product = purchasedProductsList.single { it.id == productModel.id }
                product.quantity = quantityNumber
                priceTextView.text =
                    priceTextView.context.getString(
                        R.string.price,
                        (productModel.price * quantityNumber)
                    )
            }
        }
    }

    inner class CartViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(productModel: ProductModel) = with(binding) {
            setVariable(BR.productItem, productModel)
            setVariable(BR.productListener, productListener)
            setVariable(BR.adapter, this@CartAdapter)
        }
    }

    interface ProductListener {
        fun onProductDelete(productModel: ProductModel)
    }
}
