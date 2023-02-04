package com.androiddevs.mvvmnewsapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log.e
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.MainActivity
import com.androiddevs.mvvmnewsapp.ui.NewsViewModel
import com.androiddevs.mvvmnewsapp.ui.Repository.NewsRepository
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment :Fragment(R.layout.fragment_article){

    lateinit var viewModel: NewsViewModel
    val args:ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel=(activity as MainActivity).viewModel

        val article=args.article

        webView.apply {
            webViewClient= WebViewClient()
            loadUrl(article.url)
        }

        viewModel.isArtAlreadySaved(article.url).observe(viewLifecycleOwner, Observer{value->
            fab.isClickable = value < 1
            e("num",value.toString())
        })

        fab.setOnClickListener {
            viewModel.saveArticle(article)
            Toast.makeText(requireContext(), "article saved!", Toast.LENGTH_SHORT).show()
        }
    }
}