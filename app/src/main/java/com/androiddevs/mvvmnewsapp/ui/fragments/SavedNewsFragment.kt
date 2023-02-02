package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.MainActivity
import com.androiddevs.mvvmnewsapp.ui.NewsViewModel
import com.androiddevs.mvvmnewsapp.ui.adapters.NewsAdapter

class SavedNewsFragment :Fragment(R.layout.fragment_saved_news){
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel=(activity as MainActivity).viewModel

//        newsAdapter.setOnItemClickListener {
//            val bundle =Bundle().apply {
//                putSerializable("article",it)
//            }
//            findNavController().navigate(
//                R.id.action_savedNewsFragment_to_articleFragment2,
//                bundle
//            )
//        }
    }

}