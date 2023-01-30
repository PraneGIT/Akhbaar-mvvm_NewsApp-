package com.androiddevs.mvvmnewsapp.ui.models

import com.androiddevs.mvvmnewsapp.ui.models.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)