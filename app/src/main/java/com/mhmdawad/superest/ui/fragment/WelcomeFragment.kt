package com.mhmdawad.superest.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mhmdawad.superest.R
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.mhmdawad.superest.databinding.FragmentWelcomeBinding
import com.mhmdawad.superest.util.fullScreenWindow
import com.mhmdawad.superest.util.regularWindow
import com.mhmdawad.superest.util.showToast
import com.mhmdawad.superest.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* check if it the first time for the user open the app
            so if it the first time Welcome fragment still shown but if its not will run
            a function that check if user logged in with firebase authentication if he has logged in
            will open MainFragment if not will open Authentication Fragment.
         */
        val isFirstLogIn = mainViewModel.checkIfFirstLogIn()
        if(!isFirstLogIn){
            checkIfUserLoggedIn()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_welcome, container, false
        )
        binding.fragment = this
        return binding.root
    }

    // check if user logged in
    private fun checkIfUserLoggedIn() {
        val isLoggedIn = mainViewModel.checkIfUserLoggedIn()
        if(isLoggedIn){
            showToast("Open Main Fragment")
        }else{
            showToast("Open Auth Fragment")
        }
    }

    fun openSignupFragment() {
        showToast("open signup page")
    }

    //change app window to fullscreen
    override fun onResume() {
        super.onResume()
        fullScreenWindow()
    }

    // cancel fullscreen mode.
    override fun onStop() {
        super.onStop()
        regularWindow()
    }
}