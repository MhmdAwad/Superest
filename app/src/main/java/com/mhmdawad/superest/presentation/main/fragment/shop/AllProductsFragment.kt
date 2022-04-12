package com.mhmdawad.superest.presentation.main.fragment.shop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.FragmentAllProductsBinding
import com.mhmdawad.superest.model.ProductModel
import com.mhmdawad.superest.presentation.main.adapter.ProductItemsAdapter
import com.mhmdawad.superest.util.Resource
import com.mhmdawad.superest.util.extention.closeFragment
import com.mhmdawad.superest.util.extention.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllProductsFragment : Fragment(R.layout.fragment_all_products), ProductItemsAdapter.ProductListener {

    private lateinit var binding: FragmentAllProductsBinding
    private val args by navArgs<AllProductsFragmentArgs>()
    private val categoryItem by lazy { args.categoryItem }
    private val shopViewModel by activityViewModels<ShopViewModel>()


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
    }

    private fun observeListener() {
        shopViewModel.cartProductsLiveData.observe(viewLifecycleOwner, {
            when(it){
                is Resource.Success->{
                    showToast(getString(R.string.productAddToCart))
                    shopViewModel.setCartProductValue()
                }
                is Resource.Error-> showToast(it.msg!!)
            }
        })
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

    override fun addProductToCart(productModel: ProductModel) {
        shopViewModel.addProductToCart(productModel.copy())
    }
}