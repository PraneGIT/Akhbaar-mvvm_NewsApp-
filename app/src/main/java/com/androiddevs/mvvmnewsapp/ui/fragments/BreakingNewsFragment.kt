package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.util.Log.e
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.Constants.Constants
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
        var canPaginate=true

        setupRecyclerView()

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages =newsResponse.totalResults
                        if(viewModel.breakingNewsPage==totalPages/Constants.QUERY_PAGE_SIZE +2){
                            canPaginate=false
                        }
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

        rvBreakingNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    //Toast.makeText(context, "Last", Toast.LENGTH_LONG).show()
                   if(canPaginate) {
                       viewModel.getBreakingNews("in")
                   }
                }
            }
        })
    }

    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter(this)
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