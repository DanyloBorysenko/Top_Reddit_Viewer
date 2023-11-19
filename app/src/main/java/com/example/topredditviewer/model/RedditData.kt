package com.example.topredditviewer.model

import com.squareup.moshi.Json

data class RedditData(val data : DataField)
data class DataField(val children : List<Children>)
data class Children(val data: Publication)
data class Publication(val title : String, @Json(name = "media_metadata") val mediaMetaData: Map<String, MediaItem>?, val url : String, val thumbnail : String, val preview : Preview?)
data class MediaItem(val id : String, val p : List<MediaImage>)
data class MediaImage(val y: Int, val x: Int, val u: String)
data class Preview(val images: List<Image>?)
data class Image(val source : Source)
data class Source(val url: String)
