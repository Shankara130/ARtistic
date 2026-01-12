package com.example.artistic.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.artistic.data.entity.Artwork
import com.example.artistic.databinding.ItemArtworkBinding
import com.example.artistic.utils.toTimeAgo
import com.example.artistic.utils.formatCount

class ArtworkAdapter(
    private val onItemClick: (Artwork) -> Unit
) : ListAdapter<Artwork, ArtworkAdapter.ArtworkViewHolder>(ArtworkDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtworkViewHolder {
        val binding = ItemArtworkBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArtworkViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ArtworkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ArtworkViewHolder(
        private val binding: ItemArtworkBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(artwork: Artwork) {
            binding.apply {
                tvTitle.text = artwork.title
                tvDescription.text = artwork.description
                tvLikes.text = artwork.totalLikes.formatCount()
                tvTime.text = artwork.creationDate.toTimeAgo()
                
                // TODO: Load image with Glide
                // TODO: Load artist info
                
                root.setOnClickListener {
                    onItemClick(artwork)
                }
                
                btnViewAR.setOnClickListener {
                    // TODO: Open AR viewer
                }
            }
        }
    }
    
    class ArtworkDiffCallback : DiffUtil.ItemCallback<Artwork>() {
        override fun areItemsTheSame(oldItem: Artwork, newItem: Artwork): Boolean {
            return oldItem.artworkId == newItem.artworkId
        }
        
        override fun areContentsTheSame(oldItem: Artwork, newItem: Artwork): Boolean {
            return oldItem == newItem
        }
    }
}