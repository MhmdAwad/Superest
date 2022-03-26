package com.mhmdawad.superest.viewmodel

import androidx.lifecycle.ViewModel
import com.mhmdawad.superest.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    fun checkIfFirstLogIn(): Boolean = mainRepository.checkIfFirstLogIn()

    fun checkIfUserLoggedIn(): Boolean = mainRepository.checkIfUserLoggedIn()
}