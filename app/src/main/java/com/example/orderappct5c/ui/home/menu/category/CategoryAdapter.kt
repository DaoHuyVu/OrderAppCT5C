package com.example.orderappct5c.ui.home.menu.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.orderappct5c.R
import com.example.orderappct5c.databinding.CategoryItemLayoutBinding
import com.example.orderappct5c.util.API_PREFIX

class CategoryAdapter(
    private val onClick : (String) -> Any
) : ListAdapter<Category, CategoryAdapter.CategoryItemViewHolder>(diffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CategoryItemViewHolder.create(parent)

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        holder.bind(getItem(position),onClick)
    }
    class CategoryItemViewHolder(
        private val binding: CategoryItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root){
        companion object{
            fun create(parent : ViewGroup) : CategoryItemViewHolder {
                val binding = CategoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return CategoryItemViewHolder(binding)
            }
        }
        fun bind(category : Category, onClick: (String) -> Any){
            Glide
                .with(binding.root)
                .load(API_PREFIX + category.imageUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .skipMemoryCache(true)
                .error(android.R.drawable.stat_notify_error)
                .into(binding.categoryItemImage)
            binding.apply {
                root.setOnClickListener{
                    onClick.invoke(category.name)
                }
                categoryName.text = category.name
            }
        }

    }
}
private val diffCallBack = object : DiffUtil.ItemCallback<Category>(){
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}
