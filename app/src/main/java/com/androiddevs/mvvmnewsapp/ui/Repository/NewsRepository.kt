package com.androiddevs.mvvmnewsapp.ui.Repository

import com.androiddevs.mvvmnewsapp.ui.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.ui.utils.RetrofitInstance

class NewsRepository(val db:ArticleDatabase) {
    suspend fun getBreakingNews(countryCode:String,pageNo:Int) = RetrofitInstance.api.getBreakingNews(countryCode,pageNo)
}
