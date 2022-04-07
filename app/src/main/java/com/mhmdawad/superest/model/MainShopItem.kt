package com.mhmdawad.superest.model

import android.os.Parcelable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.mhmdawad.superest.presentation.main.adapter.ProductItemsAdapter
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainShopItem(
    val id: String,
    val name: String,
    val sort: Int,
    var showInSimpleStyle: Boolean,
    val list: MutableList<ProductModel> = mutableListOf()
): Parcelable

fun convertDocumentListToMainShopList(doc: MutableList<DocumentSnapshot>): MutableList<MainShopItem> {
    val shopList = mutableListOf<MainShopItem>()
    doc.forEach {
        val item = MainShopItem(
            id = it.data?.get("id").toString(),
            name = it.data?.get("name").toString(),
            showInSimpleStyle = it.data?.get("showInSimpleStyle").toString().toBoolean(),
            sort = it.data?.get("sort").toString().toInt()
        )
        shopList.add(item)
    }
    return shopList
}

