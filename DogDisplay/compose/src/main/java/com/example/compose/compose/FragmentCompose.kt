package com.example.compose.compose

import com.example.compose.R
import android.util.Log
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.compose.model.Dog
import com.example.compose.viewmodel.DogViewModel

private const val TAG = "Navigation"

@Composable
fun FragmentNavigation(
    startDestination: String = "dogList",
    dogViewModel: DogViewModel = viewModel()
) {
    // https://developer.android.com/topic/libraries/architecture/viewmodel?hl=zh-cn#sharing
    // https://developer.android.com/jetpack/compose/libraries?hl=zh-cn
    // 如果您使用 Architecture Components ViewModel 库，
    // 可以通过调用 viewModel() 函数，从任何可组合项访问 ViewModel。
    // https://developer.android.com/guide/fragments/communicate?hl=zh-cn
    // 在目的地之间传递数据
    /*
    * 通常情况下，强烈建议您仅在目的地之间传递最少量的数据。
    * 例如，您应该传递键来检索对象而不是传递对象本身，
    * 因为在 Android 上用于保存所有状态的总空间是有限的。
    * 如果您需要传递大量数据，不妨考虑使用 ViewModel（如在 Fragment 之间共享数据中所述）
    * */
    // https://developer.android.com/guide/navigation/navigation-pass-data?hl=zh-cn
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        // 定义 route 为 dogList
        // 切勿将 ViewModel 实例传递给其他可组合项，请仅传递其所需要的数据以及以参数形式执行所需逻辑的函数。
        composable("dogList") {
            val dogList by dogViewModel.dogListLiveData.observeAsState(listOf())
            // 显示DogList
            DogListFragment(navController,dogList)
        }
        // 在目的地之间传递数据
        // https://developer.android.com/guide/navigation/navigation-pass-data?hl=zh-cn#supported_argument_types
        // 向路线中添加参数占位符
        composable("dogDetail/{position}", arguments = listOf(
            navArgument("position") {
                type = NavType.IntType  //类型
            })
        ) {
            val dog by dogViewModel.dogLiveData.observeAsState()
            Log.d(TAG,"after observeAsState")
            val position = it.arguments?.getInt("position", 0)?: 0
            Log.d(TAG,"position = $position ")
            // 在viewmodel中初始化 selected dog 数据
            dogViewModel.initSelectedDog(position)
            Log.d(TAG,"dog = $dog")

            //传递 dog 数据 到 fragment,并传入回调函数 ,更新 dogList 数据
            dog?.let { it1 -> DogDetailFragment(navController,it1){ changedDog ->
                dogViewModel.updateDog(changedDog,position)
            }}
        }

    }


}


@Composable
fun DogListFragment(navController: NavController,dogList: List<Dog>) {
    DisplayDogListWithOnClick(dogList){ position, dog ->
        // 监听
        Log.d(
            TAG,"inner DisplayDogListWithOnClick, position = $position," +
                "dog = $dog")
        // 传递数据
        navController.navigate("dogDetail/$position")
    }
}


@Composable
fun DogDetailFragment(navController: NavController,selectedDog: Dog,
                      onChanged: (changedDog: Dog) -> Unit) {
    Log.d(TAG,"inner DogDetailFragment ,dog = $selectedDog")
    // 显示的Dog详情页
    Scaffold(
        // 顶部导航栏
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = selectedDog.name
                    )
                },
                backgroundColor = Color.Transparent, elevation = 0.dp,
                navigationIcon = {
                    IconButton(onClick = {
                        // 回退栈
                        navController.popBackStack()
                    }) {
                        val backIcon: Painter = painterResource(R.drawable.ic_arrow_back)
                        Icon(painter = backIcon, contentDescription = "ic_back")
                    }
                }
            )
        }
    ) {
        DisplayDogDetail(dog = selectedDog){changedDog ->
            onChanged(changedDog)
        }
    }
}