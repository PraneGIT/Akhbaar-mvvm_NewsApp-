package com.androiddevs.mvvmnewsapp.ui.models

import com.androiddevs.mvvmnewsapp.ui.models.Article

data class NewsResponse(
    var articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)