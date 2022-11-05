package com.example.paging3.recyclerview

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3.data.Article
import com.example.paging3.data.createdText
import com.example.paging3.databinding.ArticleViewholderBinding

private const val TAG = "ArticleViewHolder"
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
    fun bindItemClick(onClick : (position: Int) -> Unit) {
        Log.d(TAG,"absoluteAdapterPosition = $absoluteAdapterPosition")
        //将 absoluteAdapterPosition 数据回传
        //设置 itemView 监听
        binding.root.setOnClickListener{
            onClick(absoluteAdapterPosition)
        }
    }

}