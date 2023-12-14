package com.example.orderappct5c.ui.home.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.orderappct5c.R
import com.example.orderappct5c.databinding.CartItemLayoutBinding
import com.example.orderappct5c.ui.home.menu.itemdetail.OrderItem
import com.example.orderappct5c.util.API_PREFIX

class CartAdapter(
    private val onModify : (Long,Int) -> Unit,
    private val onRemove : (Long) -> Unit,

) : ListAdapter<OrderItem,CartAdapter.CartItemViewHolder>(diffCallBack){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CartItemViewHolder.create(parent)

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        holder.bind(getItem(position),onModify,onRemove)
    }
    class CartItemViewHolder(
        private val binding : CartItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root){
        companion object{
            fun create(parent: ViewGroup) : CartItemViewHolder{
                val binding = CartItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),parent, false)
                return CartItemViewHolder(binding)
            }
        }
        fun bind(
            item : OrderItem,
            onModify : (Long,Int) -> Unit,
            onRemove: (Long) -> Unit){
            binding.apply {
                Glide
                    .with(root)
                    .load(API_PREFIX + item.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(android.R.drawable.stat_notify_error)
                    .skipMemoryCache(true)
                    .into(itemImage)
                itemName.text = item.name
                price.text = root.context.getString(R.string.item_price,item.price)
                quantity.text = item.quantity.toString()
                incButton.setOnClickListener{
                    val quantityValue = quantity.text.toString().toInt()
                    if(quantityValue < 100){
                        onModify.invoke(item.id,quantityValue+1)
                        quantity.text = quantityValue.plus(1).toString()
                    }

                }
                removeButton.setOnClickListener{
                    onRemove.invoke(item.id)
                }
                decButton.setOnClickListener{
                    val quantityValue = quantity.text.toString().toInt()
                    if(quantityValue > 1){
                        onModify.invoke(item.id,item.quantity-1)
                        quantity.text = quantityValue.minus(1).toString()
                    }
                }
            }
        }
    }
}
private val diffCallBack = object : DiffUtil.ItemCallback<OrderItem>(){
    override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
        return oldItem.id == newItem.id && oldItem.quantity == newItem.quantity
    }

    override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
        return oldItem == newItem
    }
}