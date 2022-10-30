package com.example.compose.logic

import com.example.compose.GlobalApp
import com.example.compose.model.Dog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class DataHelper {
    /**
     * Read the data from dogs.json and parse it into Dog object list to return.
     */
    suspend fun getDogList() = withContext(Dispatchers.Default) {
        var dogs: List<Dog> = ArrayList()
        try {
            val assetsManager = GlobalApp.context.assets
            val inputReader = InputStreamReader(assetsManager.open("dogs.json"))
            val jsonString = BufferedReader(inputReader).readText()
            val typeOf = object : TypeToken<List<Dog>>() {}.type
            dogs = Gson().fromJson(jsonString, typeOf)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        dogs
    }

}