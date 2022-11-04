package com.example.paging3.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.example.paging3.data.Article
import com.example.paging3.data.createdText
import com.example.paging3.databinding.ArticleViewholderBinding

class ArticleViewHolder(
    private val binding: ArticleViewholderBinding
) : RecyclerView.ViewHolder(binding.root){
    // 把数据和视图的绑定工作都放在Holder里处理
    fun bind(article: Article) {
        with(binding){
            title.text = article.title
            description.text = article.description
            created.text = article.createdText
        }
/*        binding.apply {
            title.text = article.title
            description.text = article.description
            created.text = article.createdText
        }*/
    }

}