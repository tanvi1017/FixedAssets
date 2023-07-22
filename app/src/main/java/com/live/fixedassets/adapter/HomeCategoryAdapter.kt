package com.live.fixedassets.adapter

import android.util.Log
import com.live.fixedassets.fragments.HomeFragment
import com.live.fixedassets.model.CategDataItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.live.fixedassets.databinding.ItemHomeBinding



class HomeCategoryAdapter(private val items: MutableList<CategDataItem>, val listener: HomeFragment):RecyclerView.Adapter<HomeCategoryAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCategoryAdapter.ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }
    override fun onBindViewHolder(holder: HomeCategoryAdapter.ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }
    override fun getItemCount(): Int {
        return items.size
    }
    inner class ItemViewHolder(private val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(item: CategDataItem){
                binding.ivImage.setImageResource(item.Image)
                itemView.setOnClickListener{
                    listener.onItemClicked(layoutPosition)
                }
            }
    }
}