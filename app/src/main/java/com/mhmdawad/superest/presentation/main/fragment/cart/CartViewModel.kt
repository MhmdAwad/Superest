package com.mhmdawad.superest.presentation.main.fragment.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhmdawad.superest.data.repository.ShopRepository
import com.mhmdawad.superest.model.ProductModel
import com.mhmdawad.superest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CartViewModel
@Inject
constructor(
    private val shopRepository: ShopRepository
) : ViewModel() {

    private val _cartProductsLiveData = MutableLiveData<Resource<List<ProductModel>>>()
    val cartProductsLiveData: LiveData<Resource<List<ProductModel>>> = _cartProductsLiveData

    fun deleteProductFromCart(productModel: ProductModel){
        viewModelScope.launch(Dispatchers.IO) {
            shopRepository.deleteProductFromUserCart(productModel.id)
            withContext(Dispatchers.Main){
                getAllCartProducts()
            }
        }
    }

    fun getAllCartProducts() {
        _cartProductsLiveData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _cartProductsLiveData.postValue(
                shopRepository.getAllUserProducts()
            )
        }
    }

}