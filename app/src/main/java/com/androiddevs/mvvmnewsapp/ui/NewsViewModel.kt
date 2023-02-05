package com.androiddevs.mvvmnewsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log.e
import androidx.lifecycle.AndroidViewModel
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
import java.io.IOException

class NewsViewModel(app:Application,
    val newsRepository: NewsRepository
):AndroidViewModel(app) {
        val breakingNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        var breakingNewsPage=1
        var breakingNewsResponse :NewsResponse ?=null

        val searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        var searchNewsPage=1
        var searchNewsResponse :NewsResponse ?=null

        fun getBreakingNews(countryCode:String)= viewModelScope.launch {
          safeBreakingNewsCall(countryCode)
        }

        fun getSearchedNews(query:String)=viewModelScope.launch{
            safeSearchNewsCall(query)
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

    private suspend fun safeBreakingNewsCall(countryCode:String){
        breakingNews.postValue(Resource.Loading())
        try {

            if(hasInternetConnection()){
                val response=newsRepository.getBreakingNews(countryCode,breakingNewsPage)
                breakingNews.postValue(handlerBreakingNewsResponse(response))
            }else{
                breakingNews.postValue(Resource.Error("No Internet Connection"))
            }

        }catch (t:Throwable){
            when(t){
                is IOException ->breakingNews.postValue(Resource.Error("network error"))
                else -> breakingNews.postValue(Resource.Error(" error"))
            }
        }
    }

    private suspend fun safeSearchNewsCall(query:String){
        searchNews.postValue(Resource.Loading())
        try {

            if(hasInternetConnection()){
                val response=newsRepository.getSearchedNews(query,searchNewsPage)
                searchNews.postValue(handlerSearchNewsResponse(response))
            }else{
                searchNews.postValue(Resource.Error("No Internet Connection"))

            }

        }catch (t:Throwable){
            when(t){
                is IOException ->searchNews.postValue(Resource.Error("network error"))
                else -> searchNews.postValue(Resource.Error(" error"))
            }
        }
    }

    fun isArtAlreadySaved(artUrl:String) = newsRepository.isArtAlreadySaved(artUrl)

    private fun hasInternetConnection():Boolean{
        val connectivityManager = getApplication<NewsApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR)->true
                capabilities.hasTransport(TRANSPORT_ETHERNET)->true
                else ->false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI->true
                    TYPE_MOBILE->true
                    TYPE_ETHERNET->true
                    else->false
                }
            }
        }
        return false
    }
}