package com.example.topredditviewer.model

import com.squareup.moshi.Json

data class RedditData(val data : DataField)
data class DataField(val children : List<Children>, val after : String?, val before : String?)
data class Children(val data: Publication)
data class Publication(
    val title : String,
    @Json(name = "is_gallery") val isGallery : Boolean?,
    @Json(name = "media_metadata") val mediaMetaData: Map<String, MediaItem>?,
    @Json(name = "gallery_data") val galleryData : GalleryData?,
    @Json(name = "num_comments") val numComments : Int?,
    val author : String?,
    val thumbnail : String?,
    val preview : Preview?,
    val created : Long?,
    val url : String?)
data class GalleryData(val items : List<GalleryItem>)
data class GalleryItem(@Json(name = "media_id") val mediaId : String)
data class MediaItem(val id : String, val s : MediaImage)
data class MediaImage(val y: Int, val x: Int, val u: String)
data class Preview(val images: List<Image>?)
data class Image(val source : Source)
data class Source(val url: String)

enum class ThumbnailTypes(val value : String) {
    DEFAULT("default"), NSFW("nsfw")
}