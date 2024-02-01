package com.example.imageserch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.imageserch.databinding.ItemRecentKeywordBinding

class KeywordAdapter: ListAdapter<String, KeywordAdapter.KeywordViewHolder>(diffUtil) {

    interface OnItemClickListener {
        fun onItemClick(data: String, pos:Int)
        fun onCancelClick(data: String, pos: Int)
    }
    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRecentKeywordBinding.inflate(layoutInflater, parent, false)
        return KeywordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class KeywordViewHolder(private val binding: ItemRecentKeywordBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) = with(binding) {
            tvItemKeyword.apply {
                text = item
                setOnClickListener {
                    listener?.onItemClick(item, adapterPosition)
                }
            }
            ivItemKeyword.setOnClickListener {
                listener?.onCancelClick(item, adapterPosition)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean  = oldItem == newItem
        }
    }

}