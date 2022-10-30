package com.example.dogdisplay.logic

import com.example.dogdisplay.GlobalApp
import com.example.dogdisplay.model.Dog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.Flow

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