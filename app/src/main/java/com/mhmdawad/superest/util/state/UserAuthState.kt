package com.mhmdawad.superest.util.state

sealed class UserAuthState {

    object Success: UserAuthState()
    data class Error(val error: String): UserAuthState()
    object Loading: UserAuthState()
}