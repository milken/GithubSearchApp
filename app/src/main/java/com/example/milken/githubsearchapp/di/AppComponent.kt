package com.example.milken.githubsearchapp.di

import com.example.milken.githubsearchapp.ui.MyApp
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetModule::class])
interface AppComponent {

    fun getSearchSubComponent(searchModule: SearchModule): SearchComponent
    fun getDetailsSubComponent(detailsModule: DetailsModule): DetailsComponent

    fun inject(app: MyApp)
}