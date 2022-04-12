package com.mhmdawad.superest.util

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.format.DateFormat
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.mhmdawad.superest.R
import com.mhmdawad.superest.model.OffersModel
import com.mhmdawad.superest.model.ProductModel
import com.mhmdawad.superest.presentation.main.adapter.ImageSliderAdapter
import com.mhmdawad.superest.presentation.main.adapter.ProductItemsAdapter
import com.mhmdawad.superest.util.extention.hide
import com.mhmdawad.superest.util.extention.loadGif
import com.mhmdawad.superest.util.extention.loadImage
import com.mhmdawad.superest.util.extention.show
import com.smarteist.autoimageslider.SliderView
import java.text.SimpleDateFormat
import java.util.*


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
    if (products != null && products.isNotEmpty()) {
        val productAdapter =
            ProductItemsAdapter(products, showInSimpleStyle, showInGridView, productListener)
        rv.adapter = productAdapter
        rv.show()
    } else {
        rv.hide()
    }
}

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, link: String) {
    imageView.loadGif(R.drawable.spinner)
    if(link.isNotEmpty())
        imageView.loadImage(link)
}

@BindingAdapter("initSliderImage", "offerListener")
fun initSliderImage(
    sliderView: SliderView,
    list: List<OffersModel>,
    offerListener: ImageSliderAdapter.OfferListener
) {
    val adapter = ImageSliderAdapter(list, offerListener)
    sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
    sliderView.setSliderAdapter(adapter)
    sliderView.scrollTimeInSec = 10
    sliderView.isAutoCycle = true
    sliderView.startAutoCycle()
}

@BindingAdapter("indicatorProgressColor")
fun indicatorColor(circularProgressIndicator: CircularProgressIndicator, color: String) {
    circularProgressIndicator.setIndicatorColor(Color.parseColor(color))
}

@BindingAdapter("loadGif")
fun loadGifIntoImageView(imageView: ImageView, imageGif: Drawable) {
    imageView.loadGif(R.drawable.food_empty_gif)
}

@BindingAdapter("quantityEditText", "increasePrice")
fun changeProductQuantityValue(
    imageView: ImageView,
    quantityEditText: EditText,
    increasePrice: Boolean
) {
    imageView.setOnClickListener {
        var quantity = quantityEditText.text.toString().trim().toInt()
        if (increasePrice) {
            quantity++
        } else if (!increasePrice && quantity > 1) {
            quantity--
        }
        quantityEditText.setText(quantity.toString())
    }
}

@BindingAdapter("formatDate")
fun formatMilliSecondsDate(textView: TextView, milliseconds: Long){
    val df = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
    df.format(Date(milliseconds)).let {
        textView.text = it
    }
}