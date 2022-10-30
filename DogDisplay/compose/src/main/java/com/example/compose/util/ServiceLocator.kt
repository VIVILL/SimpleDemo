package com.example.compose.util

import com.example.compose.logic.DataHelper
import com.example.compose.logic.Repository

/**
 * ServiceLocator to provide instances that no one is responsible to create.
 */
object ServiceLocator {

    /**
     * Provide the Repository instance that ViewModel should depend on.
     */
    fun provideRepository() = Repository(provideDataHelper())

    /**
     * Provide the DataHelper instance that Repository should depend on.
     */
    private fun provideDataHelper() = DataHelper()
}