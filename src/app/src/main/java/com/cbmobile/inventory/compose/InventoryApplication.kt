package com.cbmobile.inventory.compose

import android.app.Application

//used to control how application is created and inject in different
// implementations of interfaces
class InventoryApplication : Application() {

    lateinit var container : AppContainer

    override fun onCreate(){
        super.onCreate()
        container = InventoryAppContainer(this)
    }
}