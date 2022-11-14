package com.example.jetpackdemo.fragment.home.viewpager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jetpackdemo.R
import com.example.jetpackdemo.adapter.ArticleAdapter
import com.example.jetpackdemo.adapter.BannerAdapter
import com.example.jetpackdemo.adapter.HeaderAdapter
import com.example.jetpackdemo.bean.Banner
import com.example.jetpackdemo.databinding.FragmentFirstBinding
import com.example.jetpackdemo.viewmodel.WanAndroidViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val TAG = "FirstFragment"
@AndroidEntryPoint
class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WanAndroidViewModel by viewModels()

    private val bannerAdapter = BannerAdapter(ArrayList<Banner>())
    private val headerAdapter = HeaderAdapter(bannerAdapter)
    private val articleAdapter = ArticleAdapter()
    private val concatAdapter = ConcatAdapter(headerAdapter,articleAdapter)


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"inner onCreate")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,"inner onCreateView")
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_first, container, false)
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        binding.recyclerview.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerview.context,
                (binding.recyclerview.layoutManager as LinearLayoutManager).orientation
            )
        )
        // 为RecyclerView配置adapter
        binding.recyclerview.adapter = concatAdapter

        // 更新 Banner
        viewModel.updateBannerList()

        binding.swipeLayout.setOnRefreshListener {
            // 刷新时 更新 Banner
            viewModel.updateBannerList()
            // 更新 PagingDataAdapter
            articleAdapter.refresh()
        }


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUI()
    }

    private fun subscribeUI() {
        // bannerListFlow
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bannerListStateFlow.collect{ value ->
                    // 更新 list
                    bannerAdapter.bannerList = value
                    Log.d(TAG," bannerList size = ${bannerAdapter.bannerList.size}")
                    //数据改变刷新视图
                    headerAdapter.notifyItemChanged(0)
                }
            }
        }

        // 设置 banner 无限循环滑动
        // 保证 使得息屏后 不执行定时任务
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                var counter = 0
                repeat(Int.MAX_VALUE){
                    Log.d(TAG,"inner repeat")
                    delay(3000L)
                    if (bannerAdapter.bannerList.isNotEmpty()){
                        val position = ++counter % bannerAdapter.bannerList.size
                        Log.d(TAG,"currentIndex = $counter bannerListSize = ${bannerAdapter.bannerList.size} position = $position")
                        headerAdapter.smoothScrollToPosition(position)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 获取 PagingData
                viewModel.getArticle()
                    .catch {
                        Log.d(TAG,"Exception : ${it.message}")
                    }
                    .collectLatest {
                        Log.d(TAG,"inner collectLatest")
                        // paging 使用 submitData 填充 adapter
                        articleAdapter.submitData(it)
                    }
            }
        }

        //监听paging数据刷新状态
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                articleAdapter.loadStateFlow.collectLatest {
                    Log.d(TAG, "initListener: $it")
                    binding.swipeLayout.isRefreshing = it.refresh is LoadState.Loading
                }
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FirstFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FirstFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}