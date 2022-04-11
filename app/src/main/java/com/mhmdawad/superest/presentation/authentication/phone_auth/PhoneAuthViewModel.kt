package com.mhmdawad.superest.presentation.authentication.phone_auth

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthCredential
import com.mhmdawad.superest.data.repository.AuthenticationRepository
import com.mhmdawad.superest.model.UserInfoModel
import com.mhmdawad.superest.util.Resource
import com.mhmdawad.superest.util.state.MainAuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneAuthViewModel
@Inject
constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _phoneAuthLiveData = MutableLiveData<MainAuthState>()
    val phoneMainAuthLiveData: LiveData<MainAuthState>
        get() = _phoneAuthLiveData
    fun setPhoneAuthLiveData(mainAuthState: MainAuthState) {
        _phoneAuthLiveData.value = mainAuthState
    }

    private val _signInStatusLiveData = MutableLiveData<Resource<Unit?>>()
    val signInStatusLiveData: LiveData<Resource<Unit?>> get() = _signInStatusLiveData

    private val _userInfoLiveData = MutableLiveData<Resource<String>>()
    val userInfoLiveData: LiveData<Resource<String>> get() = _userInfoLiveData

    fun checkIfFirstAppOpened(): Boolean = authenticationRepository.checkIfFirstAppOpened()

    fun checkIfUserLoggedIn(): Boolean = authenticationRepository.checkIfUserLoggedIn()

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        _signInStatusLiveData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _signInStatusLiveData.postValue(
                authenticationRepository.signInWithCredential(
                    credential
                )
            )
        }
    }

    fun phoneAuthCallBack() = authenticationRepository.phoneAuthCallBack(_phoneAuthLiveData)

    fun uploadUserInformation(userName: String, imageUri: Uri?, userLocation: String) {
        _userInfoLiveData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _userInfoLiveData.postValue(authenticationRepository.uploadUserInformation(userName, imageUri, userLocation))
        }

    }


}