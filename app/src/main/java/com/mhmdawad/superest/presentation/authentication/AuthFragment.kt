package com.mhmdawad.superest.presentation.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.FragmentAuthenticationBinding


class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthenticationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_authentication, container, false
        )
        binding.fragment = this
        return binding.root
    }

    fun navigateToPhoneAuthFragment() {
        val action = AuthFragmentDirections.actionAuthFragmentToPhoneNumberAuthFragment()
        findNavController().navigate(action)
    }

    fun navigateToGoogleAuthFragment() {
        val action = AuthFragmentDirections.actionAuthenticationFragmentToGoogleAuthFragment()
        findNavController().navigate(action)
    }



}