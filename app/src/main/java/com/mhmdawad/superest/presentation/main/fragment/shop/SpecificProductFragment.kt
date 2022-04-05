package com.mhmdawad.superest.presentation.main.fragment.shop

import android.content.Intent
import android.os.Bundle
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
import com.mhmdawad.superest.util.extention.closeFragment
import com.mhmdawad.superest.util.extention.loadGif
import com.mhmdawad.superest.util.extention.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpecificProductFragment : Fragment() {

    private val args by navArgs<SpecificProductFragmentArgs>()
    private val productModel by lazy { args.productModel }
    private lateinit var binding: FragmentSpecificProductBinding
    private val shopViewModel by activityViewModels<ShopViewModel>()
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
        shopViewModel.favoriteLiveData(productModel.id).observe(viewLifecycleOwner, {product->
            if(product != null){
                binding.favoriteProductImageView.loadGif(
                    R.drawable.favorite_gif_start,
                    R.drawable.favorite_png
                )
            }else{
                binding.favoriteProductImageView.setImageResource(R.drawable.favorite_gif)
            }
        })
    }

    private fun initViews() {
        binding.apply {
            productQuantityEditText.addTextChangedListener {
                val quantity = productQuantityEditText.text.toString()
                if (quantity.isNotEmpty()) {
                    val quantityNumber = quantity.toInt()
                    if (quantityNumber > 0)
                        productPriceTextView.text =
                            getString(R.string.price, (productModel.price * quantityNumber))
                }
            }
            specificProductImage.transitionName = productModel.image
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
        var quantity = binding.productQuantityEditText.text.toString().trim().toInt()
        if (increasePrice) {
            quantity++
        } else if (!increasePrice && quantity > 1) {
            quantity--
        }
        binding.productQuantityEditText.setText(quantity.toString())
    }

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