package com.example.paging3.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.paging3.data.Article
import com.example.paging3.databinding.ArticleViewholderBinding

class ArticleAdapter: /*ListAdapter*/PagingDataAdapter<Article, ArticleViewHolder>(ARTICLE_DIFF_CALLBACK) {

    companion object {
        private val ARTICLE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ArticleViewholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        // 通过 getItem 获取数据
        val tile = getItem(position)
        if (tile != null) {
            holder.bind(tile)
            holder.bindItemClick(onClick)
        }
    }

    private lateinit var onClick: (position: Int) -> Unit

    fun setOnItemClickListener(onClick : (position: Int) -> Unit){
        this.onClick = onClick
    }
}