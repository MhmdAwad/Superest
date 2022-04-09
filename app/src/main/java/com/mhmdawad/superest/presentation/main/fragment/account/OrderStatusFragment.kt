package com.mhmdawad.superest.presentation.main.fragment.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.FragmentOrderStatusBinding
import com.mhmdawad.superest.util.extention.closeFragment

class OrderStatusFragment : Fragment() {

    private lateinit var binding: FragmentOrderStatusBinding
    private val args by navArgs<OrderStatusFragmentArgs>()
    private val mIsOrderSubmitted by lazy { args.isOrderSubmitted }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_status, container, false)
        return binding.run {
            fragment = this@OrderStatusFragment
            isOrderSubmitted = mIsOrderSubmitted
            root
        }
    }

    fun trackOrTryAgain(){
        if(mIsOrderSubmitted){
            navigateToTrackOrderFragment()
        }else{
            closeFragment()
        }
    }

    private fun navigateToTrackOrderFragment() {
        val action = OrderStatusFragmentDirections.actionOrderStatusFragmentToTrackOrdersFragment()
        findNavController().navigate(action)
    }

    fun backToHome(){
       val action = OrderStatusFragmentDirections.actionOrderStatusFragmentToShopFragment()
        findNavController().navigate(action)
    }
}