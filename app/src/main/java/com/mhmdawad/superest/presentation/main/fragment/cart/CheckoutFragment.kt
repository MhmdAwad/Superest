package com.mhmdawad.superest.presentation.main.fragment.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.FragmentCheckoutBinding
import com.mhmdawad.superest.model.CheckoutModel
import com.mhmdawad.superest.presentation.authentication.UserInfoViewModel
import com.mhmdawad.superest.util.Resource
import com.mhmdawad.superest.util.extention.closeFragment
import com.mhmdawad.superest.util.extention.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CheckoutFragment : BottomSheetDialogFragment() {
    private val userInfoViewModel by activityViewModels<UserInfoViewModel>()
    private lateinit var binding: FragmentCheckoutBinding
    private val args by navArgs<CheckoutFragmentArgs>()
    private val totalCost by lazy { args.totalCost }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkout, container, false)
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
    }

    private fun observeListener() {
        userInfoViewModel.userInformationLiveData.observe(viewLifecycleOwner, { userInfo ->
            when (userInfo) {
                is Resource.Success -> {
                    val checkoutModel =
                        CheckoutModel(userInfo.data?.userLocationName!!, totalCost, "Payment")
                    binding.checkoutModel = checkoutModel
                }
                is Resource.Error -> {
                    showToast(userInfo.msg!!)
                }
            }
        })
    }

    fun selectPaymentMethod() {

    }

    fun changeDeliveryAddress() {

    }

    fun orderNow() {

    }

    fun closeDialog() {
        closeFragment()
    }
}