package com.example.topredditviewer.model

data class RedditData(val data : DataField)
data class DataField(val children : List<ChildrenField>)
data class ChildrenField(val data: Publication)
data class Publication(val title : String, val url : String)
