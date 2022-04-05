package com.mhmdawad.superest.presentation.main.fragment.shop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.FragmentAllProductsBinding
import com.mhmdawad.superest.model.ProductModel
import com.mhmdawad.superest.presentation.main.adapter.ProductItemsAdapter
import com.mhmdawad.superest.util.extention.closeFragment
import dagger.hilt.android.AndroidEntryPoint


class AllProductsFragment : Fragment(R.layout.fragment_all_products), ProductItemsAdapter.ProductListener {

    private lateinit var binding: FragmentAllProductsBinding
    private val args by navArgs<AllProductsFragmentArgs>()
    private val categoryItem by lazy { args.categoryItem }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_products, container, false)
        binding.fragment = this
        binding.categoryItem = categoryItem
        binding.productClickListener = this
        return binding.root
    }

    fun backPressFragment() {
        closeFragment()
    }

    override fun onProductClick(productModel: ProductModel, transitionImageView: ImageView) {
        // add transition to image view when open specific product fragment.
        val extras = FragmentNavigatorExtras(
            transitionImageView to productModel.image
        )
        val action =
            AllProductsFragmentDirections.actionAllProductsFragmentToSpecificProductFragment(productModel)
        findNavController().navigate(action, extras)

    }
}