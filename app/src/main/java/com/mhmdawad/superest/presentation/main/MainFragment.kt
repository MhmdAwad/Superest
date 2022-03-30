package com.mhmdawad.superest.presentation.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mhmdawad.superest.R
import com.mhmdawad.superest.util.extention.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
    }

    private fun observeListener() {
        mainViewModel.userHasInfoLiveData.observe(viewLifecycleOwner, { isUserHasInfo->
            if(!isUserHasInfo){
                navigateToCreateUserInfoFragment()
            }
        })
    }

    private fun navigateToCreateUserInfoFragment() {
        val action = MainFragmentDirections.actionMainFragmentToCreateUserInfoFragment()
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.checkIfUserHasInfo()
    }

}