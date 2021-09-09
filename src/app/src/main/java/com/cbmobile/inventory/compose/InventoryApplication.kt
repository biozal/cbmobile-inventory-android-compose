package com.cbmobile.inventory.compose

import android.app.Application
import com.cbmobile.inventory.compose.data.AppContainer
import com.cbmobile.inventory.compose.data.AppContainerImp

//used to control how application is created and inject in different implementations of interfaces
class InventoryApplication : Application() {

    lateinit var container : AppContainer

    override fun onCreate(){
        super.onCreate()
        container = AppContainerImp(this)
    }
}