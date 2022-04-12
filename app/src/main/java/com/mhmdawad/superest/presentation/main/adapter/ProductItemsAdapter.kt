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

class ProductItemsAdapter(
    private val productsList: List<ProductModel>,
    private val showInSimpleStyle: Boolean,
    private val showInGridView: Boolean = false,
    private val listener: ProductListener
) : RecyclerView.Adapter<ProductItemsAdapter.MainProductViewHolder>() {


    override fun getItemViewType(position: Int): Int {
        return if (showInSimpleStyle)
            SIMPLE_SHOP_LAYOUT
        else
            ADVANCED_SHOP_LAYOUT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainProductViewHolder {
        if (showInGridView)
            return MainProductViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.product_items_grid_rv_layout, parent, false
                )
            )
        if (viewType == ADVANCED_SHOP_LAYOUT)
            return MainProductViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.advanced_product_items_rv_layout, parent, false
                )
            )

        return MainProductViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.product_items_rv_layout, parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: MainProductViewHolder, position: Int) {
        holder.bind(productsList[position])
    }

    override fun getItemCount(): Int = if (productsList.size <= 4) productsList.size else 4


    inner class MainProductViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(productModel: ProductModel) = with(binding) {
            setVariable(BR.product, productModel)
            setVariable(BR.productListener, listener)
        }
    }

    interface ProductListener{
        fun onProductClick(productModel: ProductModel, transitionImageView: ImageView)
        fun addProductToCart(productModel: ProductModel)
    }
}
