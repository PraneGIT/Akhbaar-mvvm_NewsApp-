package com.androiddevs.mvvmnewsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.ui.Repository.NewsRepository
import com.androiddevs.mvvmnewsapp.ui.models.NewsResponse
import com.androiddevs.mvvmnewsapp.ui.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
):ViewModel() {
        val breakingNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        var breakingNewsPage=1

        val searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        val searchNewsPage=1

        fun getBreakingNews(countryCode:String)= viewModelScope.launch {
            breakingNews.postValue(Resource.Loading())
            val response=newsRepository.getBreakingNews(countryCode,breakingNewsPage)
            breakingNews.postValue(handlerBreakingNewsResponse(response))
        }

        fun getSearchedNews(query:String)=viewModelScope.launch{
            searchNews.postValue(Resource.Loading())
            val response=newsRepository.getSearchedNews(query,searchNewsPage)
            searchNews.postValue(handlerBreakingNewsResponse(response))

        }

    init {
        getBreakingNews("in")
    }

    private fun handlerBreakingNewsResponse(response:Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}