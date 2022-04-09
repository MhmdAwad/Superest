package com.mhmdawad.superest.presentation.main.fragment.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhmdawad.superest.data.repository.ShopRepository
import com.mhmdawad.superest.model.PaymentModel
import com.mhmdawad.superest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel
@Inject
constructor(
    private val shopRepository: ShopRepository
) : ViewModel() {

    private val _paymentIntentClientSecret = MutableLiveData<Resource<String>>()
    val paymentIntentClientSecretLiveData: LiveData<Resource<String>> = _paymentIntentClientSecret

    fun createPaymentIntent(
        paymentModel: PaymentModel
    ) {
        _paymentIntentClientSecret.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _paymentIntentClientSecret.postValue(shopRepository.createPaymentIntent(paymentModel))
        }
    }
}