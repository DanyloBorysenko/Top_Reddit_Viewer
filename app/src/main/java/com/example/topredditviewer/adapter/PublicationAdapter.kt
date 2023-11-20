package com.example.topredditviewer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.topredditviewer.R
import com.example.topredditviewer.databinding.PublicationCardBinding
import com.example.topredditviewer.model.Publication
import java.util.concurrent.TimeUnit

class PublicationAdapter(private val publications : List<Publication>, private val onClick : (publication: Publication) -> Unit) : RecyclerView.Adapter<PublicationAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(private val binding: PublicationCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(publication: Publication) {
            binding.publicationTitleTextView.text = publication.title
            val imgUri = publication.thumbnail?.toUri()?.buildUpon()?.scheme("https")?.build()
            binding.publicationImageView.load(imgUri) {
            }
            binding.publicationAuthorTextView.text = publication.author
            binding.publicationCommentsCountTextView.text = publication.numComments.toString()
            binding.publicationTimeCreatedTextView.text = getDateInTimeAgoFormat(publication.created)
            binding.publicationImageView.setOnClickListener {
                onClick.invoke(publication)
            }
        }
        private fun getDateInTimeAgoFormat(timeStamp: Long?) : String {
            var formattedDate = ""
            if (timeStamp == null) {
                return formattedDate
            }
            val context = itemView.context
            val dateInMilliseconds = timeStamp * 1000
            val now = System.currentTimeMillis()
            val diff = now - dateInMilliseconds
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
            val hours = TimeUnit.MILLISECONDS.toHours(diff)
            val days = TimeUnit.MILLISECONDS.toDays(diff)
            formattedDate = when {
                minutes < 1 -> {context.getString(R.string.just_now_text)}
                hours < 1 -> {context.getString(R.string.minutes_ago, minutes)}
                hours < 2 -> {context.getString(R.string.hour_ago)}
                hours < 24 -> {context.getString(R.string.hours_ago, hours)}
                days < 2 -> {context.getString(R.string.yesterday_text)}
                else -> {context.getString(R.string.days, days)}
            }
            return formattedDate
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