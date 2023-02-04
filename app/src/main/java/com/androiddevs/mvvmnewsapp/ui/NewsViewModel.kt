package com.androiddevs.mvvmnewsapp.ui

import android.util.Log.e
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.ui.Constants.Constants
import com.androiddevs.mvvmnewsapp.ui.Repository.NewsRepository
import com.androiddevs.mvvmnewsapp.ui.models.Article
import com.androiddevs.mvvmnewsapp.ui.models.NewsResponse
import com.androiddevs.mvvmnewsapp.ui.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
):ViewModel() {
        val breakingNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        var breakingNewsPage=1
        var breakingNewsResponse :NewsResponse ?=null

        val searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        var searchNewsPage=1
        var searchNewsResponse :NewsResponse ?=null

        fun getBreakingNews(countryCode:String)= viewModelScope.launch {
            breakingNews.postValue(Resource.Loading())
            val response=newsRepository.getBreakingNews(countryCode,breakingNewsPage)
            breakingNews.postValue(handlerBreakingNewsResponse(response))

        }

        fun getSearchedNews(query:String)=viewModelScope.launch{
            searchNews.postValue(Resource.Loading())
            val response=newsRepository.getSearchedNews(query,searchNewsPage)
            searchNews.postValue(handlerSearchNewsResponse(response))
        }

    init {
        getBreakingNews("in")
    }

    private fun handlerBreakingNewsResponse(response:Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse->
                breakingNewsPage++
                if(breakingNewsResponse ==null){
                    breakingNewsResponse=resultResponse

                }else{
                    val oldArticles =breakingNewsResponse?.articles
                    val newArticles =resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }

                return Resource.Success(breakingNewsResponse ?:resultResponse)
            }
        }

        e("fck",response.code().toString())

        return Resource.Error(response.message())
    }

    private fun handlerSearchNewsResponse(response:Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article)=viewModelScope.launch {
        newsRepository.insertUpdate(article)
    }

    fun deleteArticle(article: Article) =viewModelScope.launch {
        newsRepository.deleteArticles(article)
    }

    fun getSavedNews()= newsRepository.getAllArticles()

    fun isArtAlreadySaved(artUrl:String) = newsRepository.isArtAlreadySaved(artUrl)
}