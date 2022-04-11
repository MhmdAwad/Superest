package com.mhmdawad.superest.presentation.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhmdawad.superest.data.repository.AuthenticationRepository
import com.mhmdawad.superest.data.repository.ShopRepository
import com.mhmdawad.superest.model.UserInfoModel
import com.mhmdawad.superest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel
@Inject
    constructor(
    private val authenticationRepository: AuthenticationRepository
    ): ViewModel() {

    private val _userInformation = MutableLiveData<Resource<UserInfoModel>>()
    val userInformationLiveData : LiveData<Resource<UserInfoModel>> = _userInformation

    private val _userLocationLiveData = MutableLiveData<String?>(null)
    val userLocationLiveData: LiveData<String?> = _userLocationLiveData

    init {
        getUserInformation()
    }

    fun setUserLocation(location: String) {
        _userLocationLiveData.value = location
    }

    private fun getUserInformation(){
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.getUserInformation(_userInformation)
        }
    }

    fun changUserLocation(location: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = authenticationRepository.changeUserLocation(location!!)
            if(result)
                getUserInformation()
        }
    }
}