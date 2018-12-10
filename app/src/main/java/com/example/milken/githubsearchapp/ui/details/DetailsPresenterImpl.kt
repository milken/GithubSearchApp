package com.example.milken.githubsearchapp.ui.details

import com.example.milken.githubsearchapp.data.apis.GithubUserDetailsApi
import com.example.milken.githubsearchapp.data.models.User

class DetailsPresenterImpl(val githubUserDetailsApi: GithubUserDetailsApi) :
    DetailsContract.Presenter {

    private lateinit var view: DetailsContract.View
    private lateinit var user: User

    override fun setView(view: DetailsContract.View) {
        this.view = view
    }

    override fun setUser(user: User) {
        this.user = user
    }
}