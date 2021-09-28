package com.cbmobile.inventory.compose

import android.app.Application
import com.cbmobile.inventory.compose.data.AppContainer
import com.cbmobile.inventory.compose.data.AppContainerImp
import dagger.hilt.android.HiltAndroidApp

//used to control how application is created and inject in different implementations of interfaces
@HiltAndroidApp
class InventoryApplication : Application() {

    lateinit var container : AppContainer

    override fun onCreate(){
        super.onCreate()
        container = AppContainerImp(this)
    }
}