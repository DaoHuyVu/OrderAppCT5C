package com.example.orderappct5c.ui.home.personal.history

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.setMargins
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.orderappct5c.R
import com.example.orderappct5c.api.order.OrderDetailsHistoryView
import com.example.orderappct5c.databinding.OrderDetailLayoutBinding

class OrderDetailAdapter(
    private val callback : (Long) -> Unit
) : ListAdapter<OrderDetailsHistoryView,OrderDetailAdapter.OrderDetailViewHolder>(diffCallback){


    class OrderDetailViewHolder(
        private val binding : OrderDetailLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root){
        companion object {
            fun create(parent : ViewGroup) : OrderDetailViewHolder{
                val binding = OrderDetailLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return OrderDetailViewHolder(binding)
            }
        }
        fun bind(orders: OrderDetailsHistoryView,callback: (Long) -> Unit){
            binding.apply {
                orderId.text = root.context.getString(R.string.order_id,orders.id)
                orderAddress.text = root.context.getString(R.string.order_destination,orders.address)
                orderLocation.text = root.context.getString(R.string.order_location,orders.location)
                orderPrice.text = root.context.getString(R.string.total,orders.price)
                orderCreatedAt.text = root.context.getString(R.string.order_created_at,orders.createdAt)
                orderStatus.text = root.context.getString(R.string.order_status,orders.status)
                root.setOnClickListener {
                    callback.invoke(orders.id)
                }
                val params = MarginLayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
                params.setMargins(16)
                root.layoutParams = params
                root.background = AppCompatResources.getDrawable(root.context,R.drawable.order_item_style)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OrderDetailViewHolder.create(parent)

    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        holder.bind(getItem(position),callback)
    }
}
internal val diffCallback = object : DiffUtil.ItemCallback<OrderDetailsHistoryView>(){
    override fun areItemsTheSame(
        oldItem: OrderDetailsHistoryView,
        newItem: OrderDetailsHistoryView
    ): Boolean {
        return oldItem.id == newItem.id && oldItem.status == newItem.status
    }

    override fun areContentsTheSame(
        oldItem: OrderDetailsHistoryView,
        newItem: OrderDetailsHistoryView
    ): Boolean {
        return oldItem == newItem
    }

}
