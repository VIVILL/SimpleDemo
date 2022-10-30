package com.example.dogdisplay.util

import com.example.dogdisplay.logic.DataHelper
import com.example.dogdisplay.logic.Repository

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