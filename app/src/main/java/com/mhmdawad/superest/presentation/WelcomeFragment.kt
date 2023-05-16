package com.mhmdawad.superest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.FragmentWelcomeBinding
import com.mhmdawad.superest.presentation.authentication.phone_auth.PhoneAuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    private val authViewModel: PhoneAuthViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* check if it the first time for the user open the app
            so if it the first time Welcome fragment still shown but if its not will run
            a function that check if user logged in with firebase authentication if he has logged in
            will open MainFragment if not will open Authentication Fragment.
         */
        CoroutineScope(Dispatchers.IO).apply {
            launch(Dispatchers.Main){
                println("?@@@@ ${Thread.currentThread().name}")
            }
            launch {
                println("?@@@@2 ${Thread.currentThread().name}")
                withContext(Dispatchers.Main){
                    println("@@@@@ HELLO WITH CONTEXT")
                    delay(5000)
                }
                println("@@@@@ HELLO 1 ${Thread.currentThread().name}")
                launch(Dispatchers.Main.immediate) {
                    println("@@@@@ HELLO LAUNCH")
                    delay(5000)
                }
                println("@@@@@ HELLO 2 ${Thread.currentThread().name}")

            }
        }
        val isFirstLogIn = authViewModel.checkIfFirstAppOpened()
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

    // check if user logged in and open main fragment if user logged in
    // or authentication fragment if not.
    private fun checkIfUserLoggedIn() {
        val isLoggedIn = authViewModel.checkIfUserLoggedIn()
        if(isLoggedIn){
            navigateToMainFragment()
        }else{
            navigateToAuthenticationFragment()
        }
    }

    private fun navigateToAuthenticationFragment() {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToAuthFragment()
        findNavController().navigate(action)
    }

    private fun navigateToMainFragment() {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToMainFragment()
        findNavController().navigate(action)
    }

    fun openSignupFragment() {
        navigateToAuthenticationFragment()
    }
}