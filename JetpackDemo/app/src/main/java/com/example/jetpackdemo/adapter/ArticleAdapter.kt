package com.example.jetpackdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.jetpackdemo.databinding.ItemArticleBinding
import com.example.jetpackdemo.bean.Article

class ArticleAdapter: PagingDataAdapter<Article, ArticleAdapter.ArticleViewHolder>(ARTICLE_DIFF_CALLBACK){
    class ArticleViewHolder (
        private val binding: ItemArticleBinding
    ) : RecyclerView.ViewHolder(binding.root){

        // 把数据和视图的绑定工作都放在Holder里处理
        fun bind(article: Article) {
            with(binding){
                title.text = article.title
                if(article.author.isEmpty()){
                    description.text = article.shareUser
                }else{
                    description.text = article.author
                }
                created.text = article.niceDate
            }
        }
    }

    companion object {
        private val ARTICLE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        // 通过 getItem 获取数据
        val article = getItem(position)
        if (article != null) {
            holder.bind(article)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }

}