package com.mhmdawad.superest.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mhmdawad.superest.BR
import com.mhmdawad.superest.R
import com.mhmdawad.superest.model.ProductModel
import com.mhmdawad.superest.util.ADVANCED_SHOP_LAYOUT
import com.mhmdawad.superest.util.SIMPLE_SHOP_LAYOUT

class OrderProductsAdapter(
    private val productsList: List<ProductModel>
) : RecyclerView.Adapter<OrderProductsAdapter.OrderProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderProductViewHolder =
        OrderProductViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.order_products_items_rv, parent, false
            )
        )


    override fun onBindViewHolder(holder: OrderProductViewHolder, position: Int) {
        holder.bind(productsList[position])
    }

    override fun getItemCount(): Int = productsList.size


    inner class OrderProductViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(productModel: ProductModel) = with(binding) {
            setVariable(BR.product, productModel)
        }
    }

    interface ProductListener {
        fun onProductClick(productModel: ProductModel, transitionImageView: ImageView)
    }
}
