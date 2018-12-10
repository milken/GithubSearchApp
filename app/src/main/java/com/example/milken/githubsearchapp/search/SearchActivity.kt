package com.example.milken.githubsearchapp.search

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.milken.githubsearchapp.MyApp
import com.example.milken.githubsearchapp.R
import com.example.milken.githubsearchapp.di.SearchModule
import javax.inject.Inject

class SearchActivity : AppCompatActivity(), SearchContract.View {

    @Inject
    lateinit var presenter: SearchContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        initDagger()

        presenter.setView(this)
        presenter.viewSetUp()
    }

    private fun initDagger() {
        (application as MyApp)
            .appComponent
            .getSearchSubComponent(SearchModule())
            .inject(this)
    }


    override fun showSearchList() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgressBar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgressBar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
