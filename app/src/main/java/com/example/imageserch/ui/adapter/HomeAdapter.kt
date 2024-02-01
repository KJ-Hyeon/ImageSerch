package com.example.imageserch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.imageserch.R
import com.example.imageserch.data.SearchItem
import com.example.imageserch.databinding.ItemImageBinding
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class HomeAdapter : ListAdapter<SearchItem, HomeAdapter.SearchViewHolder>(diffUtil) {

    interface OnItemClickListener {
        fun onLikeClick(pos: Int, data: SearchItem, iv: ImageView, lottie: LottieAnimationView)
    }
    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SearchViewHolder(ItemImageBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun String.setTime(): String {
        val dateTime = OffsetDateTime.parse(this)
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return dateTime.format(dateFormat)
    }

    inner class SearchViewHolder(private val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchItem) {
            with(binding) {
                if (item.type == "image") itemImageTag.text = "[이미지]"
                else itemImageTag.text = "[비디오]"
                itemImageTitle.text = item.title
                itemImageDate.text = item.dateTime.setTime()
                itemImageLike.setImageResource(if (item.like) R.drawable.like_fill else R.drawable.like)

                Glide.with(itemImageImage)
                    .load(item.thumbnail)
                    .into(itemImageImage)
                itemImageLike.setOnClickListener {
                    listener?.onLikeClick(adapterPosition, item ,itemImageLike, itemLottieLike)
                }
            }
        }
    }
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SearchItem>() {
            override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return oldItem.thumbnail == newItem.thumbnail
            }

            override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}