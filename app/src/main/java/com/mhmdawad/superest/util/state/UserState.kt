//package com.mhmdawad.superest.util.state
//
//import com.mhmdawad.superest.model.UserInfoModel
//
//sealed class UserState {
//
//    data class  Success(val data: UserInfoModel? = null): UserState()
//    data class Error(val error: String): UserState()
//    object Loading: UserState()
//}