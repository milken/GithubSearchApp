package com.example.milken.githubsearchapp.di

import com.example.milken.githubsearchapp.ui.details.DetailsActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [DetailsModule::class])
interface DetailsComponent {

    fun inject (detailsActivity: DetailsActivity)
}