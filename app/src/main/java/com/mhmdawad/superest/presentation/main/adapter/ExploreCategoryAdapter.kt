package com.mhmdawad.superest.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mhmdawad.superest.BR
import com.mhmdawad.superest.R
import com.mhmdawad.superest.model.CategoryItem
import javax.inject.Inject


class ExploreCategoryAdapter
@Inject
constructor() : RecyclerView.Adapter<ExploreCategoryAdapter.SearchProductViewHolder>() {


    private val categoryList = mutableListOf<CategoryItem>()
    private lateinit var categoryListener: CategoryListener
    fun addCategoryItems(list: List<CategoryItem>, listener: CategoryListener) {
        categoryList.clear()
        categoryList.addAll(list)
        categoryListener = listener
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchProductViewHolder {
        return SearchProductViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.search_product_category_grid_layout, parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: SearchProductViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int = categoryList.size


    inner class SearchProductViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryItem: CategoryItem) = with(binding) {
            setVariable(BR.categoryListener, categoryListener)
            setVariable(BR.categoryItem, categoryItem)
        }
    }

    interface CategoryListener {
        fun onCategoryClick(categoryItem: CategoryItem)
    }

}