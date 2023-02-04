package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.Constants.Constants
import com.androiddevs.mvvmnewsapp.ui.MainActivity
import com.androiddevs.mvvmnewsapp.ui.NewsViewModel
import com.androiddevs.mvvmnewsapp.ui.adapters.NewsAdapter
import com.androiddevs.mvvmnewsapp.ui.utils.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_breaking_news.paginationProgressBar
import kotlinx.android.synthetic.main.fragment_search_news.*

class SearchNewsFragment :Fragment(R.layout.fragment_search_news){

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel=(activity as MainActivity).viewModel

        var canPaginate=true

        etSearch.addTextChangedListener {
           var query:String = etSearch.text.toString()

            viewModel.getSearchedNews(query)

            setupRecyclerView()

            viewModel.searchNews.observe(viewLifecycleOwner, Observer { response->
                when(response){
                    is Resource.Success->{
                        hideProgressBar()
                        response.data?.let { newsResponse ->
                            newsAdapter.differ.submitList(newsResponse.articles.toList())

                        }
                    }
                    is Resource.Error->{
                        hideProgressBar()
                        response.message?.let {
                            Log.e("search news fragment:",it)
                        }
                    }
                    is Resource.Loading->{
                        showProgressBar()
                    }
                }
            })

        }

//        rvSearchNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (!recyclerView.canScrollVertically(1)) {
//                    Toast.makeText(context, "Last", Toast.LENGTH_SHORT).show()
//                    if(canPaginate) {
//                        viewModel.getSearchedNews(etSearch.text.toString())
//                    }
//                }
//            }
//        })

    }

    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter(this)
        rvSearchNews.adapter=newsAdapter
        rvSearchNews.layoutManager= LinearLayoutManager(activity)
    }

    private fun hideProgressBar(){
        paginationProgressBar.visibility=View.INVISIBLE
    }
    private fun showProgressBar(){
        paginationProgressBar.visibility=View.VISIBLE
    }
}