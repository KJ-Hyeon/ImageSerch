package com.example.imageserch.ui.adapter

import android.app.DownloadManager.Request
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.decode.DataSource

import com.airbnb.lottie.LottieAnimationView
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
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

    inner class SearchViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchItem) = with(binding) {
            if (item.type == "image") itemImageTag.text = "[이미지]"
            else itemImageTag.text = "[비디오]"
            itemImageTitle.text = item.title
            itemImageDate.text = item.dateTime.setTime()
            itemImageLike.setImageResource(if (item.like) R.drawable.like_fill else R.drawable.like)
            itemImageLike.setOnClickListener {
                listener?.onLikeClick(adapterPosition, item, itemImageLike, itemLottieLike)
            }
            itemImageImage.load(item.thumbnail) {
                listener(coilListener)
            }
        }
        private val coilListener = object : coil.request.Request.Listener {
            override fun onStart(request: coil.request.Request) {
                super.onStart(request)
                binding.itemImageLoading.isVisible = true
            }

            override fun onSuccess(request: coil.request.Request, source: DataSource) {
                super.onSuccess(request, source)
                binding.itemImageLoading.isVisible = false
                binding.itemImageError.isVisible = false
                binding.itemImageLike.isVisible = true
            }

            override fun onError(request: coil.request.Request, throwable: Throwable) {
                binding.itemImageLoading.isVisible = false
                binding.itemImageLike.isVisible = false
                binding.itemImageError.isVisible = true

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