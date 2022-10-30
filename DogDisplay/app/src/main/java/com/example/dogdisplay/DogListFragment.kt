package com.example.dogdisplay

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dogdisplay.adapter.DogListAdapter
import com.example.dogdisplay.viewmodel.DogViewModel



class DogListFragment : Fragment() {

    private  val dogViewModel: DogViewModel by activityViewModels()

    private lateinit var dogRecyclerView: RecyclerView

    private lateinit var adapter: DogListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_dog_list, container, false)
        dogRecyclerView =  view.findViewById(R.id.recyclerView) as RecyclerView
        dogRecyclerView.layoutManager = LinearLayoutManager(context)
        // 为RecyclerView配置adapter
        adapter = DogListAdapter()

        adapter.setOnItemClickListener {
            Log.d("adapter","position = $it")
            val host: NavHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
            val navController = host.navController
            // 设置显示 dogDetailFragment
            val args = Bundle()
            args.putInt("position", it)
            //跳转到带参数的 fragment
            //调用navigate跳转之前先判断
            if (navController.currentDestination?.id == R.id.dogListFragment) {
                navController.navigate(R.id.action_dogListFragment_to_dogDetailFragment,args);
            }
        }
        dogRecyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startObserver()
    }

    private fun startObserver() {
        dogViewModel.dogListLiveData.observe(viewLifecycleOwner){
            Log.d("DogListFragment","setList")
            adapter.setList(it)
            //数据改变刷新视图
          //  adapter.notifyDataSetChanged()
            // 使用 submitList() 及时更新列表
            it?.let {
                adapter.submitList(it)
            }
        }
    }
}