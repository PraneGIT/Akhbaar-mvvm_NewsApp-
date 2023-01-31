package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.MainActivity
import com.androiddevs.mvvmnewsapp.ui.NewsViewModel
import com.androiddevs.mvvmnewsapp.ui.adapters.NewsAdapter
import com.androiddevs.mvvmnewsapp.ui.utils.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment :Fragment(R.layout.fragment_breaking_news){
    lateinit var viewModel:NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel=(activity as MainActivity).viewModel

        setupRecyclerView()
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error->{
                    hideProgressBar()
                    response.message?.let {
                        Log.e("breaking news fragment:",it)
                    }
                }
                is Resource.Loading->{
                    showProgressBar()
                }
            }

        })
    }

    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        rvBreakingNews.adapter=newsAdapter
        rvBreakingNews.layoutManager=LinearLayoutManager(activity)
    }

    private fun hideProgressBar(){
        paginationProgressBar.visibility=View.INVISIBLE
    }
    private fun showProgressBar(){
        paginationProgressBar.visibility=View.VISIBLE
    }
}