package com.example.topredditviewer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.topredditviewer.databinding.PublicationCardBinding
import com.example.topredditviewer.model.Publication

class PublicationAdapter(private val publications : List<Publication>, private val onClick : (publication: Publication) -> Unit) : RecyclerView.Adapter<PublicationAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(private val binding: PublicationCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(publication: Publication) {
            binding.publicationTitleTextView.text = publication.title
            val imgUri = publication.thumbnail.toUri().buildUpon().scheme("https").build()
            binding.publicationImageView.load(imgUri) {
            }
            binding.publicationImageView.setOnClickListener {
                onClick.invoke(publication)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            PublicationCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return publications.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = publications[position]
        holder.bind(item)
    }
}