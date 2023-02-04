package com.androiddevs.mvvmnewsapp.ui.api

import com.androiddevs.mvvmnewsapp.ui.Constants.Constants
import com.androiddevs.mvvmnewsapp.ui.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface newsAPI {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode:String="in",
        @Query("page")
        page:Int=1,
        @Query("apiKey")
        apiKey:String="75bce8f0756b40a59b27d0ba8b2a5a0f"
        ):Response<NewsResponse>

    @GET("v2/everything")
    suspend fun getSearchedNews(
        @Query("q")
        searchQuery:String,
        @Query("page")
        page:Int=1,
        @Query("apiKey")
        apiKey:String="75bce8f0756b40a59b27d0ba8b2a5a0f"
    ):Response<NewsResponse>

}