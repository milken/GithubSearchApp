package com.example.milken.githubsearchapp.search

import android.util.Log
import com.example.milken.githubsearchapp.data.apis.GithubSearchApi

class SearchPresenterImpl(
    val githubSearchApi: GithubSearchApi
) : SearchContract.Presenter {

    private lateinit var view: SearchContract.View

    override fun setView(view: SearchContract.View) {
        this.view = view
    }

    override fun viewSetUp() {
        Log.d("MyTag","setUp!")
    }

    override fun viewDestroyed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun textChanged(text: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}