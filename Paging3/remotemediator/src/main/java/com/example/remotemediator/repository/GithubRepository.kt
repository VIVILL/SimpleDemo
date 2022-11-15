package com.example.remotemediator.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.remotemediator.api.GithubApi
import com.example.remotemediator.bean.Repo
import com.example.remotemediator.db.RepoDatabase
import com.example.remotemediator.paging.GithubRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubRepository @Inject constructor(
    private val githubApi: GithubApi,
    private val database: RepoDatabase
){
    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }

    /**
     * Search repositories whose names match the query, exposed as a stream of data that will emit
     * every time we get more data from the network.
     */
    fun getSearchResultStream(query: String): Flow<PagingData<Repo>> {
        Log.d("GithubRepository", "New query: $query")

        // appending '%' so we can allow other characters to be before and after the query string
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory = {
            database.reposDao().reposByName(dbQuery)
        }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = GithubRemoteMediator(
                query,
                githubApi,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

}