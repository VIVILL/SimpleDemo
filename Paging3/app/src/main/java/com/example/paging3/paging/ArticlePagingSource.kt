package com.example.paging3.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.paging3.data.Article
import kotlinx.coroutines.delay
import java.lang.Math.max
import java.time.LocalDateTime
import java.util.*

private const val STARTING_KEY = 0
private val firstArticleCreatedTime = LocalDateTime.now()
private const val LOAD_DELAY_MILLIS = 3_000L

private const val TAG = "ArticlePagingSource"
class ArticlePagingSource : PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        Log.d(TAG,"inner load")
        // Start paging with the STARTING_KEY if this is the first load
        val start = params.key ?: STARTING_KEY
        // Load as many items as hinted by params.loadSize
        val range = start.until(start + params.loadSize)
        Log.d(TAG,"start = $start params.loadSize = ${params.loadSize}" +
                "  range = $range , range first = ${range.first} " +
                " range last = ${range.last}")
        // 添加一点延迟以模拟加载过程
        if (start != STARTING_KEY) delay(LOAD_DELAY_MILLIS)

        return LoadResult.Page(
            data = range.map { number ->
                Article(
                    // Generate consecutive increasing numbers as the article id
                    id = number,
                    title = "Article $number",
                    description = "This describes article $number",
                    //created = Date()
                     created = firstArticleCreatedTime.minusDays(number.toLong())
                )
            },
            // Make sure we don't try to load items behind the STARTING_KEY
            prevKey = when (start) {
                STARTING_KEY -> null
                else -> ensureValidKey(key = range.first - params.loadSize)
            },
            nextKey = range.last + 1
        )
    }

    // The refresh key is used for the initial load of the next PagingSource, after invalidation
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        Log.d(TAG,"inner getRefreshKey")
        // In our case we grab the item closest to the anchor position
        // then return its id - (state.config.pageSize / 2) as a buffer
        val anchorPosition = state.anchorPosition ?: return null
        Log.d(TAG,"anchorPosition = $anchorPosition")
        val article = state.closestItemToPosition(anchorPosition) ?: return null
        Log.d(TAG,"article = $article article.id = ${article.id} " +
                "state.config.pageSize = ${state.config.pageSize / 2}")
        return ensureValidKey(key = article.id - (state.config.pageSize / 2))
    }

    private fun ensureValidKey(key: Int): Int{
        Log.d(TAG,"STARTING_KEY = $STARTING_KEY , key = $key " +
                "max return " + max(STARTING_KEY, key))
        return max(STARTING_KEY, key)
    }

}