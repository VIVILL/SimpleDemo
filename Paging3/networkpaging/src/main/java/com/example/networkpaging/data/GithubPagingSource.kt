
package com.example.networkpaging.data

import android.util.Log
import androidx.paging.*
import com.example.networkpaging.api.GithubService
import com.example.networkpaging.api.IN_QUALIFIER
import com.example.networkpaging.data.GithubRepository.Companion.NETWORK_PAGE_SIZE
import com.example.networkpaging.model.Repo
import retrofit2.HttpException
import java.io.IOException

// GitHub page API is 1 based: https://developer.github.com/v3/#pagination
private const val GITHUB_STARTING_PAGE_INDEX = 1
private const val TAG = "GithubPagingSource"

class GithubPagingSource(
    private val service: GithubService,
    private val query: String
) : PagingSource<Int, Repo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        Log.d(TAG,"inner load")
        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
        val apiQuery = query + IN_QUALIFIER
        return try {
            val response = service.searchRepos(apiQuery, position, params.loadSize)
            val repos = response.items
            val nextKey = if (repos.isEmpty()) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                // 初次加载 params.loadSize 为 NETWORK_PAGE_SIZE 3倍数量
                // position = 1 params.loadSize = 90 NETWORK_PAGE_SIZE = 30
                // 后续 paging 加载 params.loadSize 为 NETWORK_PAGE_SIZE 数量
                // position = 5 params.loadSize = 30 NETWORK_PAGE_SIZE = 30
                // position = 4 params.loadSize = 30 NETWORK_PAGE_SIZE = 30
                Log.d(TAG,"position = $position params.loadSize = ${params.loadSize}" +
                        " NETWORK_PAGE_SIZE = $NETWORK_PAGE_SIZE")
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            //初次加载 nextKey = 4 position = 1
            //第二次 load nextKey = 5 position = 4
            //第三次 load  nextKey = 6 position = 5
            Log.d(TAG,"nextKey = $nextKey position = $position")

            LoadResult.Page(
                data = repos,
                prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}
