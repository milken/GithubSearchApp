package com.example.milken.githubsearchapp

import android.app.Application
import com.example.milken.githubsearchapp.di.AppComponent
import com.example.milken.githubsearchapp.di.DaggerAppComponent
import com.example.milken.githubsearchapp.di.NetModule

class MyApp: Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        setupComponent()
    }

    private fun setupComponent() {
        component = DaggerAppComponent
            .builder()
            .netModule(NetModule())
            .build()
    }
}