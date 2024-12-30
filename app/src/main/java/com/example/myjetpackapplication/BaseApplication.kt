package com.example.myjetpackapplication

import android.app.Application
import com.example.myjetpackapplication.di.AppComponent
import com.example.myjetpackapplication.di.DaggerAppComponent

class BaseApplication : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
        appComponent.inject(this)
    }
}


