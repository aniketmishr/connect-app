package com.example.connect

import android.app.Application
import com.example.connect.data.AppContainer
import com.example.connect.data.Container

class ConnectApplication: Application() {

    lateinit var container : Container

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }

}