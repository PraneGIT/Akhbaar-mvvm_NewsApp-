package com.androiddevs.mvvmnewsapp.ui.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.androiddevs.mvvmnewsapp.ui.models.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdate(article: Article):Long

    @Query("SELECT * FROM articles")
    fun getAllArticles():LiveData<List<Article>>

    @Query("select COUNT(*) from articles where url = :artUrl")
    fun isArtAlreadySaved(artUrl:String):LiveData<Long>

    @Delete
    suspend fun deleteArticle(article: Article)
}