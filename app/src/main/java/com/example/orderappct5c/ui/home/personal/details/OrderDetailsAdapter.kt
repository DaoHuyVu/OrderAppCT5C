package com.example.orderappct5c.ui.home.personal.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.orderappct5c.R
import com.example.orderappct5c.databinding.OrderDetailsItemBinding
import com.example.orderappct5c.ui.home.menu.itemdetail.OrderItemDto
import com.example.orderappct5c.util.API_PREFIX

class OrderDetailsAdapter : ListAdapter<OrderItemDto, OrderDetailsAdapter.OrderItemViewHolder>(diffCallback){
    class OrderItemViewHolder(
        private val binding : OrderDetailsItemBinding
    ) : RecyclerView.ViewHolder(binding.root){
        companion object {
            fun create(parent : ViewGroup) : OrderItemViewHolder{
                val binding = OrderDetailsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return OrderItemViewHolder(binding)
            }
        }
        fun bind(order : OrderItemDto){
            binding.apply {
                Glide
                    .with(root)
                    .load(API_PREFIX + order.imageUrl)
                    .error(android.R.drawable.stat_notify_error)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemImage)
                itemName.text = order.name
                price.text = root.context.getString(R.string.item_price,order.price)
                quantity.text = order.quantity.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OrderItemViewHolder.create(parent)

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
internal val diffCallback = object : DiffUtil.ItemCallback<OrderItemDto>(){
    override fun areItemsTheSame(
        oldItem: OrderItemDto,
        newItem: OrderItemDto
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: OrderItemDto,
        newItem: OrderItemDto
    ): Boolean {
        return oldItem == newItem
    }

}
