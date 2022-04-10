package com.mhmdawad.superest.model

import android.os.Parcelable
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
        it["productsList"] = productsList
    }
    return map
}