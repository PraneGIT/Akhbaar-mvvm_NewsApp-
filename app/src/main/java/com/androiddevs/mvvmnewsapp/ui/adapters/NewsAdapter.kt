package com.androiddevs.mvvmnewsapp.ui.adapters

import android.os.Bundle
import android.util.Log.e
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.fragments.BreakingNewsFragment
import com.androiddevs.mvvmnewsapp.ui.fragments.SavedNewsFragment
import com.androiddevs.mvvmnewsapp.ui.fragments.SearchNewsFragment
import com.androiddevs.mvvmnewsapp.ui.models.Article
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter(val fragment:Fragment):RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

    private val differCallBack = object :DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem==newItem
        }

    }
    val differ=AsyncListDiffer(this,differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_article_preview,
            parent,false
        ))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article= differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage)
                .into(ivArticleImage)

            tvSource.text=article.source?.name
            tvDescription.text=article.description
            tvPublishedAt.text=article.publishedAt
            tvTitle.text=article.title


            setOnClickListener {

                val bundle = Bundle().apply {
                    putSerializable("article",article)
                }
                when(fragment){
                    is BreakingNewsFragment ->{
                        findNavController().navigate(
                            R.id.action_breakingNewsFragment_to_articleFragment,
                            bundle
                        )
                    }
                    is SearchNewsFragment ->{
                        findNavController().navigate(
                            R.id.action_searchNewsFragment_to_articleFragment,
                            bundle
                        )
                    }
                    is SavedNewsFragment ->{
                        findNavController().navigate(
                            R.id.
                            action_savedNewsFragment_to_articleFragment2,
                            bundle
                        )
                    }

                }

            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

//    private var onItemClickListener:((Article)->Unit) ?=null
//
//    fun setOnItemClickListener(listener:(Article)->Unit){
//        onItemClickListener=listener
//    }
}