package com.example.imageserch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.bumptech.glide.Glide
import com.example.imageserch.data.HomeData
import com.example.imageserch.data.Image
import com.example.imageserch.data.ImageResponse
import com.example.imageserch.databinding.ItemImageBinding

class HomeAdapter : ListAdapter<HomeData, HomeAdapter.HomeViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HomeViewHolder(ItemImageBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(currentList[position] as Image)
    }

    inner class HomeViewHolder(private val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Image) {
            with(binding) {
                itemImageTitle.text = item.display_sitename
                itemImageDate.text = item.datetime
                Glide.with(itemImageImage)
                    .load(item.thumbnail_url)
                    .into(itemImageImage)
            }
        }

    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<HomeData>() {
            override fun areItemsTheSame(oldItem: HomeData, newItem: HomeData): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: HomeData, newItem: HomeData): Boolean {
                return oldItem == newItem
            }
        }
    }
}