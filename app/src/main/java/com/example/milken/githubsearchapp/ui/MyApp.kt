package com.example.milken.githubsearchapp.ui

import android.app.Application
import com.example.milken.githubsearchapp.di.AppComponent
import com.example.milken.githubsearchapp.di.DaggerAppComponent

class MyApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        setupComponent()
    }

    private fun setupComponent() {
        appComponent = DaggerAppComponent.create() // no dependencies = create
    }
}