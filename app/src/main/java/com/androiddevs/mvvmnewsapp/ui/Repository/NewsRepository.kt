package com.androiddevs.mvvmnewsapp.ui.Repository

import com.androiddevs.mvvmnewsapp.ui.db.ArticleDao
import com.androiddevs.mvvmnewsapp.ui.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.ui.models.Article
import com.androiddevs.mvvmnewsapp.ui.utils.RetrofitInstance

class NewsRepository(val db:ArticleDatabase) {
    suspend fun getBreakingNews(countryCode:String,pageNo:Int) = RetrofitInstance.api.getBreakingNews(countryCode,pageNo)

    suspend fun getSearchedNews(query:String,pageNo:Int) = RetrofitInstance.api.getSearchedNews(query,pageNo)

    suspend fun insertUpdate(article: Article) =  db.getArticleDao().insertUpdate(article)

    fun getAllArticles()=db.getArticleDao().getAllArticles()

    suspend fun deleteArticles(article: Article) = db.getArticleDao().deleteArticle(article)

    fun isArtAlreadySaved(artUrl:String)=db.getArticleDao().isArtAlreadySaved(artUrl)

}
