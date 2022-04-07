package com.mhmdawad.superest.model

import com.google.firebase.firestore.DocumentSnapshot

data class CategoryItem(
    val id: String,
    val name: String,
    val image: String,
)

fun convertDocumentsToCategoryList(documents: List<DocumentSnapshot>): List<CategoryItem> {
    val list = mutableListOf<CategoryItem>()
    documents.forEach {
        list.add(
            CategoryItem(it["id"].toString(), it["name"].toString(), it["image"].toString())
        )
    }
    return list
}

fun convertMapToCategoryItem(map: Map<String, Any>): CategoryItem =
    CategoryItem(map["id"].toString(), map["name"].toString(), map["image"].toString())

fun CategoryItem.convertToMainShopItem(products: MutableList<ProductModel>): MainShopItem =
    MainShopItem(id, name, 50, false, products)