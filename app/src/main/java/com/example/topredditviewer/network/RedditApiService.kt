package com.example.topredditviewer.network

import com.example.topredditviewer.model.RedditData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private val BASE_URL = "https://www.reddit.com/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface RedditApiService {
    @GET("top.json")
    suspend fun getTopThumbnails() : RedditData
}
object RedditApi {
    val retrofitService :  RedditApiService by lazy {
        retrofit.create(RedditApiService::class.java)
    }
}