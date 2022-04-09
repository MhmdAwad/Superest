package com.mhmdawad.superest.util


const val FIRST_LOGGED_IN_APP = "FIRST_LOGGED_IN_APP"
const val USERS_COLLECTION = "Users"
const val CART_COLLECTION = "Cart"
const val EXPLAINED_PERMISSION = "EXPLAINED"
const val PERMISSION_ANNOTATION = "PERMISSION"
const val DISPLAY_DIALOG = "ALERT_DIALOG"
const val LOADING_ANNOTATION = "LOADING"
const val BASE_LATITUDE = 26.0
const val BASE_LONGITUDE = 32.0
const val LOCATION_ZOOM = 20f
const val COUNT_DOWN_DELAY = 60000L
const val COUNT_DOWN_INTERVAL = 1000L
const val SHOP_LIST = "ShopList"
const val CATEGORY = "Category"
const val PRODUCTS = "Products"
const val ITEMS = "Items"
const val INCOMPLETE_ORDERS = "IncompleteOrders"
const val SIMPLE_SHOP_LAYOUT = 667
const val ADVANCED_SHOP_LAYOUT = 660
const val OFFERS = "Offers"
const val FAVORITE_DATABASE = "FavoriteDatabase"
const val BACKEND_URL = "https://floating-springs-34147.herokuapp.com/"
private val colorList= mutableListOf<String>()
fun pickColor():String {
    if(colorList.isEmpty()) {
        colorList.add("#80E3B98E")
        colorList.add("#8095ECB5")
        colorList.add("#80F3AE9F")
        colorList.add("#80D3B0E0")
        colorList.add("#80B7DFF5")
        colorList.add("#80FDE598")
        colorList.add("#80EA9BB9")
        colorList.add("#80A091E3")
    }
    return colorList.random()
}
