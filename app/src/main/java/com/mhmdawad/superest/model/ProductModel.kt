package com.mhmdawad.superest.model

import android.os.Parcelable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentSnapshot
import com.mhmdawad.superest.util.extention.loadImage
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class ProductModel(
    @PrimaryKey
    val id: String,
    val name: String,
    val image: String,
    val calories: Double,
    val carb: Double,
    val fat: Double,
    val detail: String,
    val protein: Double,
    val price: Double,
    var quantity: Int,
    val quantityType: String,
    val category: String,
): Parcelable


fun convertMapToProductModel(map: Map<String, Any>): ProductModel {
    return ProductModel(
        map["id"].toString(),
        map["name"].toString(),
        map["image"].toString(),
        map["calories"].toString().toDouble(),
        map["carb"].toString().toDouble(),
        map["fat"].toString().toDouble(),
        map["detail"].toString(),
        map["protein"].toString().toDouble(),
        map["price"].toString().toDouble(),
        map["quantity"].toString().toDouble().toInt(),
        map["quantityType"].toString(),
        map["category"].toString(),
    )
}
fun convertArrayMapToProductList(map: ArrayList<Map<String, Any>>): List<ProductModel> {
    val list = mutableListOf<ProductModel>()
    map.forEach {
        list.add(convertMapToProductModel(it))
    }
    return list
}

fun convertDocumentToProductList(document: List<DocumentSnapshot>): MutableList<ProductModel> {
    val list = mutableListOf<ProductModel>()
    document.forEach { map->
     list.add(ProductModel(
         map["id"].toString(),
         map["name"].toString(),
         map["image"].toString(),
         map["calories"].toString().toDouble(),
         map["carb"].toString().toDouble(),
         map["fat"].toString().toDouble(),
         map["detail"].toString(),
         map["protein"].toString().toDouble(),
         map["price"].toString().toDouble(),
         map["quantity"].toString().toDouble().toInt(),
         map["quantityType"].toString(),
         map["category"].toString(),
     ))
    }
    return list
}

fun ProductModel.toMap(): Map<String, Any>{
    val map = mutableMapOf<String, Any>()
    map["id"] = id
    map["name"] = name
    map["image"] = image
    map["calories"] = calories
    map["carb"] = carb
    map["fat"] = fat
    map["detail"] = detail
    map["protein"] = protein
    map["price"] = price
    map["quantity"] = quantity
    map["quantityType"] = quantityType
    map["category"] = category
    return map
}
