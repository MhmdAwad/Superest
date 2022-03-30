package com.mhmdawad.superest.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhmdawad.superest.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val mainRepository: MainRepository
):
ViewModel() {
    private val _userHasInfoLiveData = MutableLiveData<Boolean>()
    val userHasInfoLiveData: LiveData<Boolean> get() = _userHasInfoLiveData

    fun checkIfUserHasInfo(){
        _userHasInfoLiveData.value = true
        viewModelScope.launch(Dispatchers.IO) {
            _userHasInfoLiveData.postValue(mainRepository.checkIfUserHasInfo())
        }
    }
}