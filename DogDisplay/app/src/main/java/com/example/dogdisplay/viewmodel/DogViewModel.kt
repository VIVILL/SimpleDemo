package com.example.dogdisplay.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.dogdisplay.logic.Repository
import com.example.dogdisplay.model.Dog
import com.example.dogdisplay.util.ServiceLocator
import kotlinx.coroutines.launch

private const val TAG = "DogViewModel"
class DogViewModel: ViewModel(){
    val dogListLiveData: MutableLiveData<List<Dog>> = MutableLiveData()

    private val repository: Repository = ServiceLocator.provideRepository()

    private val dogList  = mutableListOf<Dog>()

    init {
        // 初始化数据
        viewModelScope.launch {
            dogList.addAll(
                //处理耗时操作
                repository.getDogList()
            )
            Log.d(TAG,"inner init DogViewModel " +
                    "size = ${dogList.size}")
            // 更新数据
            dogListLiveData.postValue(dogList)
        }
    }

    val dogLiveData: MutableLiveData<Dog> = MutableLiveData()

    fun initSelectedDog(position: Int){
        Log.d(TAG,"inner initSelectedDog")
        val selectedDog = dogList[position]
        dogLiveData.postValue(selectedDog)
    }

    fun updateDog(dog: Dog,position: Int){
        Log.d(TAG,"inner updateDog")
        dogList[position] = dog
        // 更新数据
        dogListLiveData.postValue(dogList)
    }

}