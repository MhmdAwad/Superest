package com.mhmdawad.superest.model

import android.os.Parcelable
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.parcelize.Parcelize


@Parcelize
data class PhoneVerificationModel (
    val verificationId: String,
    val verificationToken: PhoneAuthProvider.ForceResendingToken,
    val phoneNumber: String
): Parcelable