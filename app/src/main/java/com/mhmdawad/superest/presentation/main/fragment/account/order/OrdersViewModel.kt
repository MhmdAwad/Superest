package com.mhmdawad.superest.presentation.main.fragment.account.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhmdawad.superest.data.repository.ShopRepository
import com.mhmdawad.superest.model.OrderModel
import com.mhmdawad.superest.model.UserInfoModel
import com.mhmdawad.superest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel
@Inject
constructor(
    private val shopRepository: ShopRepository
) : ViewModel() {

    private val _userOrdersLiveData = MutableLiveData<Resource<List<OrderModel>>>()
    val userOrdersLiveData: LiveData<Resource<List<OrderModel>>> = _userOrdersLiveData

    init {
        getUserOrders()
    }

    private fun getUserOrders(){
        _userOrdersLiveData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _userOrdersLiveData.postValue(
                shopRepository.getUserOrders()
            )
        }
    }

}