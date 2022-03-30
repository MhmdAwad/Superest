package com.mhmdawad.superest.presentation.authentication

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthCredential
import com.mhmdawad.superest.data.repository.AuthenticationRepository
import com.mhmdawad.superest.util.state.MainAuthState
import com.mhmdawad.superest.util.state.UserAuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel
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

    private val _signInStatusLiveData = MutableLiveData<UserAuthState>()
    val signInStatusLiveData: LiveData<UserAuthState> get() = _signInStatusLiveData

    private val _userInfoLiveData = MutableLiveData<UserAuthState>()
    val userInfoLiveData: LiveData<UserAuthState> get() = _userInfoLiveData

    private val _userLocationLiveData = MutableLiveData<String?>(null)
    val userLocationLiveData: LiveData<String?> = _userLocationLiveData
    fun setUserLocation(location: String) {
        _userLocationLiveData.value = location
    }

    fun checkIfFirstAppOpened(): Boolean = authenticationRepository.checkIfFirstAppOpened()

    fun checkIfUserLoggedIn(): Boolean = authenticationRepository.checkIfUserLoggedIn()

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        _signInStatusLiveData.value = UserAuthState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            _signInStatusLiveData.postValue(
                authenticationRepository.signInWithPhoneAuthCredential(
                    credential
                )
            )
        }

    }

    fun phoneAuthCallBack() = authenticationRepository.phoneAuthCallBack(_phoneAuthLiveData)

    fun uploadUserInformation(userName: String, imageUri: Uri, userLocation: String) {
        _userInfoLiveData.value = UserAuthState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            _userInfoLiveData.postValue(authenticationRepository.uploadUserInformation(userName, imageUri, userLocation))
        }

    }


}