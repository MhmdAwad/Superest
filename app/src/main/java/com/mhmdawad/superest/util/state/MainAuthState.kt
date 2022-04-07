package com.mhmdawad.superest.util.state

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

sealed class MainAuthState {

    object Idle: MainAuthState()
    object Loading: MainAuthState()
    data class SuccessWithCredential(val data: PhoneAuthCredential): MainAuthState()
    data class SuccessWithCode(val verificationId: String, val verificationToken: PhoneAuthProvider.ForceResendingToken): MainAuthState()
    data class Error(val error: FirebaseException): MainAuthState()
}