package com.example.topredditviewer.network

import com.example.topredditviewer.model.RedditData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://www.reddit.com/"
private const val LIMIT = 10
private const val RAW_JSON = 1

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface RedditApiService {
    @GET("top.json")
    suspend fun loadTopPublication(
        @Query(value = "limit") limit : Int = LIMIT,
        @Query(value = "raw_json") rawJson : Int = RAW_JSON,
        @Query(value = "after") after : String? = null,
        @Query(value = "before") before : String? = null,
        @Query(value = "count") count : Int? = null
    ) : RedditData
}
object RedditApi {
    val retrofitService :  RedditApiService by lazy {
        retrofit.create(RedditApiService::class.java)
    }
}