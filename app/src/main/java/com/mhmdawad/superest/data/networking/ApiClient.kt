package com.mhmdawad.superest.data.networking

import com.mhmdawad.superest.data.networking.ApiService
import com.mhmdawad.superest.model.PaymentModel
import com.mhmdawad.superest.util.Resource
import javax.inject.Inject

class ApiClient
@Inject
constructor(
    private val apiService: ApiService
) {

    suspend fun createPaymentIntent(
        paymentModel: PaymentModel
    ): Resource<String> {
        val request = apiService.createPaymentIntent(paymentModel)
        return if (!request.isSuccessful) {
            Resource.Error(request.toString())
        } else {
            val responseData = request.body()
            val paymentIntentClientSecret: String? =
                responseData?.get("clientSecret")
            Resource.Success(paymentIntentClientSecret!!)
        }
    }
}
