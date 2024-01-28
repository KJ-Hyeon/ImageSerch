package com.example.imageserch.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imageserch.data.HomeData
import com.example.imageserch.data.Image
import com.example.imageserch.data.Video
import com.example.imageserch.databinding.ItemImageBinding
import com.example.imageserch.databinding.ItemVideoBinding
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

const val ITEM_IMAGE = 1
const val ITEM_VIDEO = 2
class HomeAdapter : ListAdapter<HomeData, RecyclerView.ViewHolder>(diffUtil) {

    interface OnItemClickListener {
        fun onLikeClick(pos: Int, iv: ImageView)
    }
    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            ITEM_IMAGE -> ImageViewHolder(ItemImageBinding.inflate(layoutInflater, parent, false))
            ITEM_VIDEO -> VideoViewHolder(ItemVideoBinding.inflate(layoutInflater, parent, false))
            else -> throw IllegalArgumentException("UnKnown $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ImageViewHolder -> holder.bind(currentList[position] as Image)
            is VideoViewHolder -> holder.bind(currentList[position] as Video)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position] is Image) ITEM_IMAGE else ITEM_VIDEO
    }
    fun String.setTime(): String {
        val dateTime = OffsetDateTime.parse(this)
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return dateTime.format(dateFormat)
    }

    inner class ImageViewHolder(private val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Image) {
            with(binding) {
                itemImageTitle.text = item.display_sitename
                itemImageDate.text = item.datetime.setTime()
                Glide.with(itemImageImage)
                    .load(item.thumbnail_url)
                    .into(itemImageImage)
                itemImageLike.setOnClickListener {
                    listener?.onLikeClick(adapterPosition, binding.itemImageLike)
                }
            }
        }
    }

    inner class VideoViewHolder(private val binding: ItemVideoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Video) {
            with(binding) {
                itemVideoTitle.text = item.title
                itemVideoDate.text = item.datetime.setTime()
                Glide.with(itemVideoImage)
                    .load(item.thumbnail)
                    .into(itemVideoImage)
                itemVideoLike.setOnClickListener {
                    listener?.onLikeClick(adapterPosition, binding.itemVideoLike)
                }
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