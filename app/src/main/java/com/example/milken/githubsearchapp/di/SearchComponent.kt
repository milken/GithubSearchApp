package com.example.milken.githubsearchapp.di

import com.example.milken.githubsearchapp.ui.search.SearchActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent (modules = [SearchModule::class])
interface SearchComponent {

    fun inject (searchActivity: SearchActivity)
}