package com.androiddevs.mvvmnewsapp.ui.utils

import com.androiddevs.mvvmnewsapp.ui.Constants.Constants
import com.androiddevs.mvvmnewsapp.ui.api.newsAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitInstance {
    companion object{
        private val retrofit by lazy{
            val logging= HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client=OkHttpClient.Builder().addInterceptor(logging).build()
            Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        private val api by lazy{
             retrofit.create(newsAPI::class.java)
        }
    }
}