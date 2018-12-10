package com.example.milken.githubsearchapp.di

import com.example.milken.githubsearchapp.search.SearchActivity
import dagger.Subcomponent

@Subcomponent (modules = [SearchModule::class])
@ActivityScope
interface SearchComponent {

    fun inject (searchActivity: SearchActivity)
}