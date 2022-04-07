package com.mhmdawad.superest.presentation.main.fragment.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhmdawad.superest.data.repository.ShopRepository
import com.mhmdawad.superest.model.CategoryItem
import com.mhmdawad.superest.model.MainShopItem
import com.mhmdawad.superest.model.ProductModel
import com.mhmdawad.superest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel
@Inject
constructor(
    private val shopRepository: ShopRepository
) : ViewModel() {
    private val _categoryLiveData = MutableLiveData<Resource<List<CategoryItem>>>()
    val categoryLiveData: LiveData<Resource<List<CategoryItem>>> = _categoryLiveData

    private val _searchedProductsLiveData = MutableLiveData<Resource<List<ProductModel>>>()
    val searchedProductsLiveData: LiveData<Resource<List<ProductModel>>> = _searchedProductsLiveData

    private val _categoryProductsLiveData = MutableLiveData<Resource<MainShopItem>>()
    val categoryProductsLiveData: LiveData<Resource<MainShopItem>> = _categoryProductsLiveData


    fun getCategoryList() {
        _categoryLiveData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _categoryLiveData.postValue(
                shopRepository.getCategoryList()
            )
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

    fun getProductsByCategoryId(id: String) {
        _categoryProductsLiveData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _categoryProductsLiveData.postValue(shopRepository.getSpecificCategoryProducts(id))
            delay(500)
            _categoryProductsLiveData.postValue(Resource.Idle())
        }
    }
}