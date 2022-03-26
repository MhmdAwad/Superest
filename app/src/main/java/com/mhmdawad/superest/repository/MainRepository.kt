package com.mhmdawad.superest.repository

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.mhmdawad.superest.util.Constants
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class MainRepository
@Inject
constructor(
    private val sharedPref: SharedPreferences,
    private val firebaseAuth: FirebaseAuth
) {
    private fun changeFirstOpenApp(editor: SharedPreferences.Editor){
        editor.putBoolean(Constants.FIRST_LOGGED_IN_APP, false).apply()
    }

    private fun getFirstOpenApp(sharedPreferences: SharedPreferences): Boolean{
        return sharedPreferences.getBoolean(Constants.FIRST_LOGGED_IN_APP, true)
    }

    fun checkIfFirstLogIn(): Boolean{
        val isFirstLogApp = getFirstOpenApp(sharedPref)
        val editor = sharedPref.edit()
        if(isFirstLogApp)
            changeFirstOpenApp(editor)

        return isFirstLogApp
    }

    fun checkIfUserLoggedIn(): Boolean{
        val user = firebaseAuth.currentUser
        return user != null
    }
}