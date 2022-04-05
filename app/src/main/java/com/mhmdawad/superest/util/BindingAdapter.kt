package com.mhmdawad.superest.util

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.mhmdawad.superest.model.OffersModel
import com.mhmdawad.superest.model.ProductModel
import com.mhmdawad.superest.presentation.main.adapter.ImageSliderAdapter
import com.mhmdawad.superest.presentation.main.adapter.ProductItemsAdapter
import com.mhmdawad.superest.util.extention.loadImage
import java.util.*
import com.smarteist.autoimageslider.SliderView


@BindingAdapter("randomBackground")
fun randomBackground(view: View, item: Int?) {
    val rnd = Random()
    val color: Int = Color.argb(40, rnd.nextInt(211), rnd.nextInt(201), rnd.nextInt(221))
    view.setBackgroundColor(color)
}

@BindingAdapter(value = ["products", "showInSimpleStyle", "showInGridView", "productListener"])
fun setProducts(
    rv: RecyclerView,
    products: List<ProductModel>?,
    showInSimpleStyle: Boolean,
    showInGridView: Boolean,
    productListener:
    ProductItemsAdapter.ProductListener
) {
    if (products != null) {
        val productAdapter = ProductItemsAdapter(products, showInSimpleStyle, showInGridView, productListener)
        rv.adapter = productAdapter
    }
}

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, link: String) {
    imageView.loadImage(link)
}

@BindingAdapter("initSliderImage","offerListener")
fun initSliderImage(sliderView: SliderView, list: List<OffersModel>, offerListener: ImageSliderAdapter.OfferListener) {
    val adapter = ImageSliderAdapter(list, offerListener)
    sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
    sliderView.setSliderAdapter(adapter)
    sliderView.scrollTimeInSec = 10
    sliderView.isAutoCycle = true
    sliderView.startAutoCycle()
}

@BindingAdapter("indicatorProgressColor")
fun indicatorColor(circularProgressIndicator: CircularProgressIndicator,color: String){
    circularProgressIndicator.setIndicatorColor(Color.parseColor(color))
}