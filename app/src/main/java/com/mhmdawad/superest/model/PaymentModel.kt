package com.mhmdawad.superest.model

data class PaymentModel(
    val amount: String,
    val currency: String = "usd",
    val paymentMethodType: String = "card"
)