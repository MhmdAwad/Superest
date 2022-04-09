package com.mhmdawad.superest.model

enum class OrderEnums {

    ORDERED, SHIPPED, DELIVERED
}

data class OrderModel(
    val orderId: String,
    val userUid: String,
    val orderSubmittedTime: Long,
    val orderLocation: String,
    val orderStatus: OrderEnums,
    val productsList: List<ProductModel>
)

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