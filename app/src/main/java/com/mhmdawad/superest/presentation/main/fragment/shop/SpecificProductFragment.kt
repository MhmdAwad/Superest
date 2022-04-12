package com.mhmdawad.superest.presentation.main.fragment.shop

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.FragmentSpecificProductBinding
import com.mhmdawad.superest.model.ProductModel
import com.mhmdawad.superest.util.Resource
import com.mhmdawad.superest.util.extention.closeFragment
import com.mhmdawad.superest.util.extention.loadTimerGif
import com.mhmdawad.superest.util.extention.show
import com.mhmdawad.superest.util.extention.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpecificProductFragment : Fragment() {

    private val args by navArgs<SpecificProductFragmentArgs>()
    private val productModel by lazy { args.productModel }
    private lateinit var binding: FragmentSpecificProductBinding
    private val shopViewModel by activityViewModels<ShopViewModel>()
    private var productQuantity = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_specific_product, container, false)
        binding.apply {
            fragment = this@SpecificProductFragment
            product = productModel
            return this.root
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeListener()
    }

    private fun observeListener() {
        // check if product saved in favorite database and change icon image as following.
        shopViewModel.favoriteLiveData(productModel.id).observe(viewLifecycleOwner, { product ->
            if (product != null) {
                binding.favoriteProductImageView.loadTimerGif(R.drawable.favorite_gif_start)
            } else {
                binding.favoriteProductImageView.setImageResource(R.drawable.favorite_png)
            }
        })

        shopViewModel.cartProductsLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    showToast(getString(R.string.productAddToCart))
                    shopViewModel.setCartProductValue()
                }
                is Resource.Error -> {
                    showToast(it.msg!!)
                }
            }
        })
    }

    private fun initViews() {
        binding.apply {
            // change total price by quantity value in edit text.
            productQuantityEditText.addTextChangedListener {
                val quantity = productQuantityEditText.text.toString().trim()
                if (quantity.isNotEmpty() && TextUtils.isDigitsOnly(quantity)) {
                    productQuantity = quantity.toDouble().toInt()
                    if (productQuantity > 0) {
                        productPriceTextView.text =
                            getString(R.string.price, (productModel.price * productQuantity))
                    }
                }
            }
            specificProductImage.transitionName = productModel.image
        }
    }

    fun addProductToCart() {
        // change product quantity with what user last saved.
        val productTemp = createTempProductWithNewQuantity()
        if (productTemp != null)
            shopViewModel.addProductToCart(productTemp)
    }

    private fun createTempProductWithNewQuantity(): ProductModel? {
        val quantity = binding.productQuantityEditText.text.toString().trim()
        return if (TextUtils.isDigitsOnly(quantity)) {
            productModel.copy().let { temp ->
                temp.quantity = productQuantity
                temp
            }
        } else {
            binding.productQuantityEditText.setError(getString(R.string.productQuantityError), null)
            null
        }
    }

    fun saveProductInFavorite() {
        shopViewModel.saveProductInFavorites(productModel)
    }

    override fun onResume() {
        super.onResume()
        binding.specificProductShadow.show()
    }

    fun changeProductQuantity(increasePrice: Boolean) {
        // increase or decrease quantity value .
        var quantity = binding.productQuantityEditText.text.toString().trim().toInt()
        if (increasePrice) {
            quantity++
        } else if (!increasePrice && quantity > 1) {
            quantity--
        }
        binding.productQuantityEditText.setText(quantity.toString())
    }

    // share product with messaging app.
    fun shareProduct() {
        val intent = Intent(Intent.ACTION_SEND)
        val shareBody =
            getString(R.string.shareProduct, productModel.name, productModel.price)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(intent)
    }

    fun backPressFragment() {
        closeFragment()
    }
}