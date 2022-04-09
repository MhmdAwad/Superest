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
        binding.fragment = this
        binding.adapter = cartAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartViewModel.getAllCartProducts()
        observeListener()
    }

    private fun observeListener() {
        cartViewModel.cartProductsLiveData.observe(viewLifecycleOwner, { cartProducts ->
            when (cartProducts) {
                is Resource.Success -> {
                    if (cartProducts.data == null || cartProducts.data.isEmpty()) {
                        binding.emptyProducts.show()
                        binding.cartContainer.hide()
                    } else {
                        cartAdapter.addProducts(cartProducts.data, this)
                        binding.emptyProducts.hide()
                        binding.cartContainer.show()
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

    // add quantity with number one on all products and add old quantity to quantity type
    // and create track order and submit order and add payment method.
    fun checkOutProducts() {
        var totalPrice = 0.0
        cartAdapter.getPurchasedProducts().forEach {
            totalPrice += it.run { quantity * price }
        }
        openCheckOutDialog(totalPrice.toFloat())
    }

    override fun onProductDelete(productModel: ProductModel) {
        cartViewModel.deleteProductFromCart(productModel)
    }

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