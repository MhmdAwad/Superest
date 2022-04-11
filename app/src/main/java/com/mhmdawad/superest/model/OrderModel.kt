package com.mhmdawad.superest.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

enum class OrderEnums {

    PLACED, CONFIRMED, SHIPPED, DELIVERED
}
@Parcelize
data class OrderModel(
    val orderId: String,
    val userUid: String,
    val orderSubmittedTime: Long,
    val orderLocation: String,
    val orderStatus: OrderEnums,
    val totalCost: Float,
    val productsList: List<ProductModel>
): Parcelable

fun OrderModel.toMap(): Map<String, Any>{
    val map = mutableMapOf<String, Any>()
    map.let {
        it["orderId"] = orderId
        it["userUid"] = userUid
        it["orderSubmittedTime"] = orderSubmittedTime
        it["orderLocation"] = orderLocation
        it["orderStatus"] = orderStatus
        it["totalCost"] = totalCost
        it["productsList"] = productsList
    }
    return map
}

fun convertDocumentsToOrderList(doc: List<DocumentSnapshot>): List<OrderModel>{
    val list = mutableListOf<OrderModel>()
    doc.forEach {
        println(">>>>>>>>>>>>>>>>>>>> ${it["productsList"]}")
        list.add(
            OrderModel(
                it["orderId"].toString(),
                it["userUid"].toString(),
                it["orderSubmittedTime"].toString().toLong(),
                it["orderLocation"].toString(),
                OrderEnums.valueOf(it["orderStatus"].toString()),
                it["totalCost"].toString().toFloat(),
                convertArrayMapToProductList(it["productsList"] as ArrayList<Map<String, Any>>),
            )
        )
    }
    return list
}