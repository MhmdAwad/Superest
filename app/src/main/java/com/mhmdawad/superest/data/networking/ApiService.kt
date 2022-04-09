package com.mhmdawad.superest.data.networking

import com.mhmdawad.superest.model.PaymentModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {


    @Headers("Accept: application/json; charset=utf-8")
    @POST("create-payment-intent")
    suspend fun createPaymentIntent(@Body body: PaymentModel): Response<Map<String, String>>

}