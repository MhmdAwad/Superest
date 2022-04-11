package com.mhmdawad.superest.presentation.main.fragment.cart

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.FragmentCartBinding
import com.mhmdawad.superest.model.ProductModel
import com.mhmdawad.superest.presentation.main.adapter.CartAdapter
import com.mhmdawad.superest.util.LOADING_ANNOTATION
import com.mhmdawad.superest.util.Resource
import com.mhmdawad.superest.util.extention.hide
import com.mhmdawad.superest.util.extention.show
import com.mhmdawad.superest.util.extention.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class CartFragment : Fragment(), CartAdapter.ProductListener {

    private val cartViewModel by viewModels<CartViewModel>()
    private lateinit var binding: FragmentCartBinding
    private val cartProductsList = mutableListOf<ProductModel>()

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog

    @Inject
    lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        return binding.run {
            fragment = this@CartFragment
            adapter = cartAdapter
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartViewModel.getAllCartProducts()
        observeListener()
    }

    private fun observeListener() {
        // retrieve user cart products to show in recycler view.
        cartViewModel.cartProductsLiveData.observe(viewLifecycleOwner, { cartProducts ->
            when (cartProducts) {
                is Resource.Success -> {
                    if (cartProducts.data == null || cartProducts.data.isEmpty()) {
                        binding.apply {
                            emptyProducts.show()
                            cartContainer.hide()
                        }
                    } else {
                        cartAdapter.addProducts(cartProducts.data, this)
                        binding.apply {
                            emptyProducts.hide()
                            cartContainer.show()
                        }
                    }
                    loadingDialog.hide()
                }
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Error -> {
                    loadingDialog.hide()
                    showToast(cartProducts.msg!!)
                }
            }
        })
    }

    fun checkOutProducts() {
        openCheckOutDialog(getTotalPrice().toFloat())
    }

    private fun getTotalPrice(): Double {
        // get all total price from all products in user cart to show in checkout dialog.
        var totalPrice = 0.0
        cartAdapter.getPurchasedProducts().forEach {
            totalPrice += it.run { quantity * price }
        }
        return totalPrice
    }

    // delete specific product from user cart.
    override fun onProductDelete(productModel: ProductModel) {
        cartViewModel.deleteProductFromCart(productModel)
    }

    // pass total price and all products to checkout dialog to complete payment process and upload user order.
    private fun openCheckOutDialog(totalPrice: Float) {
        cartProductsList.clear()
        cartProductsList.addAll(cartAdapter.getPurchasedProducts())
        if (cartProductsList.isEmpty()) {
            showToast(getString(R.string.noProductsCart))
            return
        }
        val action = CartFragmentDirections.actionCartFragmentToCheckoutFragment(
            totalPrice,
            cartProductsList.toTypedArray()
        )
        findNavController().navigate(action)
    }


}