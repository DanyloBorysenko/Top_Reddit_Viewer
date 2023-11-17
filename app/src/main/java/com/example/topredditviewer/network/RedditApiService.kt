package com.example.topredditviewer.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private val BASE_URL = "https://www.reddit.com/"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface RedditApiService {
    @GET("top.json")
    suspend fun getTopThumbnails() : String
}
object RedditApi {
    val retrofitService :  RedditApiService by lazy {
        retrofit.create(RedditApiService::class.java)
    }
}