package com.mhmdawad.superest.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mhmdawad.superest.BR
import com.mhmdawad.superest.R
import com.mhmdawad.superest.model.OrderModel

class AllOrdersAdapter(
    private val orderList: List<OrderModel>,
    private val listener: OrderListener
) : RecyclerView.Adapter<AllOrdersAdapter.AllOrdersViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrdersViewHolder =
        AllOrdersViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.order_item_rv_layout,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: AllOrdersViewHolder, position: Int) {
        holder.bind(orderList[position])
    }

    override fun getItemCount(): Int = orderList.size

    inner class AllOrdersViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(orderModel: OrderModel) {
            binding.setVariable(BR.order, orderModel)
            binding.setVariable(BR.orderListener, listener)
        }
    }

    interface OrderListener{
        fun onOrderClicked(orderModel: OrderModel)
    }
}