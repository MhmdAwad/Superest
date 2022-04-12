package com.mhmdawad.superest.presentation.main.fragment.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhmdawad.superest.data.repository.ShopRepository
import com.mhmdawad.superest.model.*
import com.mhmdawad.superest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel
@Inject
constructor(
    private val shopRepository: ShopRepository
) : ViewModel() {

    private val _shopListLiveData =
        MutableLiveData<Resource<List<MainShopItem>>>(Resource.Loading())
    val shopListLiveData: LiveData<Resource<List<MainShopItem>>> get() = _shopListLiveData

    private val _offersLiveData =
        MutableLiveData<Resource<List<OffersModel>>>(Resource.Loading())
    val offersListLiveData: LiveData<Resource<List<OffersModel>>> get() = _offersLiveData

    private val _categoryLiveData =
        MutableLiveData<Resource<MainShopItem>?>(Resource.Idle())
    val categoryLiveData: LiveData<Resource<MainShopItem>?> get() = _categoryLiveData

    private val _searchedProductsLiveData = MutableLiveData<Resource<List<ProductModel>>>()
    val searchedProductsLiveData: LiveData<Resource<List<ProductModel>>> = _searchedProductsLiveData

    private val _cartProductsLiveData = MutableLiveData<Resource<Any>>()
    val cartProductsLiveData: LiveData<Resource<Any>> = _cartProductsLiveData
    fun setCartProductValue(){
        _cartProductsLiveData.value = Resource.Idle()
    }

    private var firstLoad = true

    fun favoriteLiveData(id: String) = shopRepository.getProductFromFavoriteLiveData(id)

    fun getShopList() {
        if (!firstLoad) return
        firstLoad = false
        viewModelScope.launch(Dispatchers.IO) {
            _offersLiveData.postValue(shopRepository.getOffersData())
            _shopListLiveData.postValue(shopRepository.getMainShopList())
        }
    }

    fun saveProductInFavorites(productModel: ProductModel) {
        viewModelScope.launch(Dispatchers.IO) {
            shopRepository.saveOrRemoveProductFromFavorite(productModel)
        }
    }

    fun getSpecificCategoryProducts(categoryId: String) {
        _categoryLiveData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _categoryLiveData.postValue(
                shopRepository.getSpecificCategoryProducts(categoryId)
            )
            delay(500)
            _categoryLiveData.postValue(Resource.Idle())
        }
    }

    fun getProductsHasContainName(searchName: String) {
        _searchedProductsLiveData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _searchedProductsLiveData.postValue(shopRepository.getProductsContainName(searchName))
            delay(500)
            _searchedProductsLiveData.postValue(Resource.Idle())
        }
    }

    fun addProductToCart(productModel: ProductModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _cartProductsLiveData.postValue(
                shopRepository.addProductsToCart(listOf(productModel), false)
            )
        }
    }
}