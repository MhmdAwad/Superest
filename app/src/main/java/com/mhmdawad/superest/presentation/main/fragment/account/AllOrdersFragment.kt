package com.mhmdawad.superest.presentation.main.fragment.account

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.FragmentAllOrdersBinding
import com.mhmdawad.superest.model.OrderModel
import com.mhmdawad.superest.presentation.main.adapter.AllOrdersAdapter
import com.mhmdawad.superest.util.LOADING_ANNOTATION
import com.mhmdawad.superest.util.Resource
import com.mhmdawad.superest.util.extention.closeFragment
import com.mhmdawad.superest.util.extention.hide
import com.mhmdawad.superest.util.extention.show
import com.mhmdawad.superest.util.extention.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class AllOrdersFragment : Fragment(), AllOrdersAdapter.OrderListener {

    private lateinit var binding: FragmentAllOrdersBinding
    private val ordersViewModel by viewModels<OrdersViewModel>()
    private lateinit var allOrdersAdapter: AllOrdersAdapter

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_orders, container, false)
        return binding.run {
            fragment = this@AllOrdersFragment
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
    }

    private fun observeListener() {
        ordersViewModel.userOrdersLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    if(it.data!=null && it.data.isNotEmpty()){
                        showRecyclerView(it.data)
                    }else{
                        hideRecyclerView()
                    }
                    loadingDialog.hide()
                }
                is Resource.Error -> {
                    showToast(it.msg!!)
                    hideRecyclerView()
                    loadingDialog.hide()
                }
                is Resource.Loading -> loadingDialog.show()
            }
        })
    }

    private fun hideRecyclerView() {
        binding.let {
            it.allOrdersRV.hide()
            it.emptyProducts.show()
        }
    }

    private fun showRecyclerView(data: List<OrderModel>) {
        allOrdersAdapter = AllOrdersAdapter(data.sortedBy { it.orderSubmittedTime }, this)
        binding.let {
            it.allOrdersRV.adapter = allOrdersAdapter
            it.emptyProducts.hide()
            it.allOrdersRV.show()
        }
    }

    fun backPress() = closeFragment()

    override fun onOrderClicked(orderModel: OrderModel) {
        // TODO: open products in order with details.
    }
}