package com.example.orderappct5c.ui.home.menu.menuitems

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.Target
import com.example.orderappct5c.R
import com.example.orderappct5c.data.menu.MenuItemUi
import com.example.orderappct5c.databinding.MenuItemLayoutBinding
import com.example.orderappct5c.util.API_PREFIX

class MenuItemAdapter(
    private val onClick : (Long) -> Unit
) : ListAdapter<MenuItemUi,MenuItemAdapter.MenuItemViewHolder>(diffCallBack){
    class MenuItemViewHolder(
        private val binding : MenuItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root){
        companion object{
            fun create(parent: ViewGroup) : MenuItemViewHolder{
                val binding = MenuItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return MenuItemViewHolder(binding)
            }
        }
        fun bind(menuItemUi: MenuItemUi, onClick: (Long) -> Unit){
            binding.apply {
                menuItemName.text = menuItemUi.name
                menuItemPrice.text = this.root.context.getString(R.string.item_price,menuItemUi.price)
                Glide
                    .with(this.root)
                    .load(API_PREFIX + menuItemUi.imageUrl)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(android.R.drawable.stat_notify_error)
                    .into(menuItemImage)

                root.setOnClickListener {
                    onClick.invoke(menuItemUi.id)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MenuItemViewHolder.create(parent)

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        holder.bind(getItem(position),onClick)
    }
}
private val diffCallBack = object : DiffUtil.ItemCallback<MenuItemUi>(){
    override fun areItemsTheSame(oldItem: MenuItemUi, newItem: MenuItemUi): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MenuItemUi, newItem: MenuItemUi): Boolean {
        return oldItem == newItem
    }
}