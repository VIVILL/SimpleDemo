package com.example.banner.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.banner.bean.Article
import com.example.banner.databinding.ItemArticleBinding

class ArticleViewHolder (
    private val binding: ItemArticleBinding
) : RecyclerView.ViewHolder(binding.root){
    private lateinit var article: Article

    // 把数据和视图的绑定工作都放在Holder里处理
    fun bind(article: Article) {
        this.article = article
        with(binding){
            title.text = article.title
            if(article.author.isEmpty()){
                description.text = article.shareUser
            }else{
                description.text = article.author
            }
            //article.author
            created.text = article.niceDate
        }
    }
}