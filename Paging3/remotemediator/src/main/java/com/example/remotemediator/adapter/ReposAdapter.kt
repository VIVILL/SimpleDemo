package com.example.remotemediator.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.remotemediator.R
import com.example.remotemediator.bean.Repo
import com.example.remotemediator.databinding.RepoViewItemBinding


class ReposAdapter : PagingDataAdapter<Repo, ReposAdapter.ReposViewHolder>(REPO_DIFF_CALLBACK) {
    class ReposViewHolder (
        private val binding: RepoViewItemBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(repo: Repo?) {
            if (repo == null) {
                val resources = itemView.resources
                with(binding){
                    repoName.text = resources.getString(R.string.loading)
                    repoDescription.visibility = View.GONE
                    repoLanguage.visibility = View.GONE
                    repoStars.text = resources.getString(R.string.unknown)
                    repoForks.text = resources.getString(R.string.unknown)
                }
            } else {
                showRepoData(repo)
            }
        }
        private var repo: Repo? = null

        private fun showRepoData(repo: Repo) {
            this.repo = repo
            with(binding){
                repoName.text = repo.fullName
                // if the description is missing, hide the TextView
                var descriptionVisibility = View.GONE
                if (repo.description != null) {
                    repoDescription.text = repo.description
                    descriptionVisibility = View.VISIBLE
                }
                repoDescription.visibility = descriptionVisibility

                repoStars.text = repo.stars.toString()
                repoForks.text = repo.forks.toString()

                // if the language is missing, hide the label and the value
                var languageVisibility = View.GONE
                if (!repo.language.isNullOrEmpty()) {
                    val resources = itemView.context.resources
                    repoLanguage.text = resources.getString(R.string.language, repo.language)
                    languageVisibility = View.VISIBLE
                }
                repoLanguage.visibility = languageVisibility
            }
        }

    }

    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {
        // 通过 getItem 获取数据
        val repo = getItem(position)
        if (repo != null) {
            holder.bind(repo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposViewHolder {
        return ReposViewHolder(
            RepoViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }

    companion object {
        private val REPO_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean =
                oldItem == newItem
        }
    }

}