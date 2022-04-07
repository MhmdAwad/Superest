package com.mhmdawad.superest.model

data class CheckoutModel (
    val userAddress: String,
    val totalCost: Float,
    val paymentMethod: String
        )