package com.example.topredditviewer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.topredditviewer.R
import com.example.topredditviewer.databinding.PublicationCardBinding
import com.example.topredditviewer.model.Publication
import com.example.topredditviewer.model.ThumbnailTypes
import java.util.concurrent.TimeUnit

private const val FIRST_PREVIEW_IMAGE_INDEX = 0

class PublicationAdapter(
    private val onClick: (publication: Publication) -> Unit
) : RecyclerView.Adapter<PublicationAdapter.CustomViewHolder>() {
    private var data: List<Publication> = emptyList()

    fun updateData(newData: List<Publication>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            PublicationCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    inner class CustomViewHolder(private val binding: PublicationCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(publication: Publication) {
            binding.publicationTitleTextView.text = publication.title
            binding.publicationImageView.load(getImageUrl(publication)) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.broken_image_ic)
            }
            binding.publicationAuthorTextView.text =
                itemView.context.getString(R.string.author_text, publication.author)
            binding.publicationCommentsCountTextView.text =
                itemView.context.getString(R.string.comments_text, publication.numComments)
            binding.publicationTimeCreatedTextView.text =
                getDateInTimeAgoFormat(publication.created)
            binding.publicationImageView.setOnClickListener {
                onClick.invoke(publication)
            }
        }

        private fun getDateInTimeAgoFormat(timeStamp: Long?): String {
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
                minutes < 1 -> {
                    context.getString(R.string.just_now_text)
                }

                hours < 1 -> {
                    context.getString(R.string.minutes_ago, minutes)
                }

                hours < 2 -> {
                    context.getString(R.string.hour_ago)
                }

                hours < 24 -> {
                    context.getString(R.string.hours_ago, hours)
                }

                days < 2 -> {
                    context.getString(R.string.yesterday_text)
                }

                else -> {
                    context.getString(R.string.days, days)
                }
            }
            return formattedDate
        }

        private fun getImageUrl(publication: Publication): String {
            val imageUrl = when (publication.thumbnail) {
                ThumbnailTypes.DEFAULT.value -> {
                    publication.preview?.images?.get(
                        FIRST_PREVIEW_IMAGE_INDEX
                    )?.source?.url
                }

                ThumbnailTypes.NSFW.value -> {
                    publication.preview?.images?.get(
                        FIRST_PREVIEW_IMAGE_INDEX
                    )?.source?.url
                }

                else -> {
                    publication.thumbnail
                }
            }
            return imageUrl.orEmpty()
        }
    }
}