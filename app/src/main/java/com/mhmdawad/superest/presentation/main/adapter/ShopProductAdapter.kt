package com.mhmdawad.superest.presentation.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mhmdawad.superest.BR
import com.mhmdawad.superest.R
import com.mhmdawad.superest.model.CategoryItem
import com.mhmdawad.superest.model.MainShopItem
import com.mhmdawad.superest.model.OffersModel
import com.mhmdawad.superest.util.ADVANCED_SHOP_LAYOUT
import com.mhmdawad.superest.util.SIMPLE_SHOP_LAYOUT
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject


class ShopProductAdapter(
    private val listener: MainProductListener,
    private val productClickListener: ProductItemsAdapter.ProductListener,
    private val offerClickListener: ImageSliderAdapter.OfferListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val shopList: MutableList<MainShopItem> = mutableListOf()
    fun addMainShopListItems(list: List<MainShopItem>?) {
        if (list == null || list == shopList) return
        shopList.clear()
        shopList.addAll(list.sortedBy { it.sort })
        notifyDataSetChanged()
    }

    private val offersList: MutableList<OffersModel> = mutableListOf()
    fun addOffersListItems(list: List<OffersModel>?) {
        if (list == null || list == offersList) return
        offersList.clear()
        offersList.addAll(list)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && offersList.isNotEmpty())
            ADVANCED_SHOP_LAYOUT
        else
            SIMPLE_SHOP_LAYOUT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ADVANCED_SHOP_LAYOUT)
            return HeaderProductViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.main_header_rv_item, parent, false
                )
            )
        return MainProductViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.product_type_rv_layout, parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val realPosition = if (offersList.isEmpty()) position else position - 1
        when (holder) {
            is MainProductViewHolder -> holder.bind(shopList[realPosition])
            is HeaderProductViewHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int {
        return if (offersList.isNotEmpty())
            shopList.size + 1
        else
            shopList.size
    }


    inner class MainProductViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(shopItem: MainShopItem) = with(binding) {
            if ((bindingAdapterPosition % 3) != 0)
                shopItem.showInSimpleStyle = true
            setVariable(BR.shopItem, shopItem)
            setVariable(BR.listener, listener)
            setVariable(BR.productClickListener, productClickListener)
        }
    }

    inner class HeaderProductViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() = with(binding) {
            setVariable(BR.offersList, offersList)
            setVariable(BR.offerClickListener, offerClickListener)
        }
    }

    interface MainProductListener {
        fun onSeeAllClicked(mainShopItem: MainShopItem)
    }
}