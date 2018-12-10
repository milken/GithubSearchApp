package com.example.milken.githubsearchapp.search

import com.example.milken.githubsearchapp.data.apis.GithubSearchApi

class SearchPresenterImpl(
    val githubSearchApi: GithubSearchApi
) : SearchContract.Presenter {


    override fun setView(view: SearchContract.View) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun viewSetUp() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun viewDestroyed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun textChanged(text: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}