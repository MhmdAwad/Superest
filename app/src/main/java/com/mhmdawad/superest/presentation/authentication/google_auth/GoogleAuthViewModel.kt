package com.mhmdawad.superest.presentation.authentication.google_auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.mhmdawad.superest.data.repository.AuthenticationRepository
import com.mhmdawad.superest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoogleAuthViewModel
@Inject
constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _googleAuthLiveData = MutableLiveData<Resource<Unit?>>()
    val googleAuthLiveData get() = _googleAuthLiveData

    private fun firebaseAuthWithGoogle(credential: AuthCredential) {
        viewModelScope.launch {
            _googleAuthLiveData.postValue(authenticationRepository.signInWithCredential(credential))
        }
    }

    fun handleGoogleAuthRequest(task: Task<GoogleSignInAccount>, errorMsg: String) {
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuthWithGoogle(credential)
        } catch (e: ApiException) {
            _googleAuthLiveData.value = Resource.Error(errorMsg)
        }
    }

}