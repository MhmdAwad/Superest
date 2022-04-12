package com.mhmdawad.superest.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfoModel(
    val userUid: String = "",
    val userName: String,
    val userImage: String,
    val userLocationName: String,
//    val userLongitude: String,
//    val userLatitude: String
) : Parcelable {

    fun toMapWithoutImage(): MutableMap<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["userUid"] = userUid
        map["userName"] = userName
        map["userLocationName"] = userLocationName
        return map
    }
}

fun UserInfoModel.toMap(): Map<String, Any> {
    val map = mutableMapOf<String, Any>()
    map["userUid"] = userUid
    map["userName"] = userName
    map["userImage"] = userImage
    map["userLocationName"] = userLocationName
    return map
}

fun convertMapToUserInfoModel(map: Map<String, Any>): UserInfoModel {
    return UserInfoModel(
        map["userUid"].toString(),
        map["userName"].toString(),
        map["userImage"].toString(),
        map["userLocationName"].toString()
    )
}