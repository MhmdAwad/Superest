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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel
@Inject
constructor(
    private val shopRepository: ShopRepository
) :
    ViewModel() {
    private val _userInfoLiveData = MutableLiveData<Resource<UserInfoModel>>(Resource.Loading())
    val userInfoLiveData: LiveData<Resource<UserInfoModel>> get() = _userInfoLiveData

    private val _shopListLiveData =
        MutableLiveData<Resource<List<MainShopItem>>>(Resource.Loading())
    val shopListLiveData: LiveData<Resource<List<MainShopItem>>> get() = _shopListLiveData

    private val _offersLiveData =
        MutableLiveData<Resource<List<OffersModel>>>(Resource.Loading())
    val offersListLiveData: LiveData<Resource<List<OffersModel>>> get() = _offersLiveData

    private val _categoryLiveData =
        MutableLiveData<Resource<MainShopItem>>(Resource.Loading())
    val categoryLiveData: LiveData<Resource<MainShopItem>> get() = _categoryLiveData
    fun changeCategoryLiveData() {
        _categoryLiveData.value = Resource.Error("")
    }

    private var firstLoad = true


    fun favoriteLiveData(id: String) = shopRepository.getProductFromFavoriteLiveData(id)

    fun getUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            shopRepository.getUserInformation(_userInfoLiveData)
        }
    }

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
        }
    }
}