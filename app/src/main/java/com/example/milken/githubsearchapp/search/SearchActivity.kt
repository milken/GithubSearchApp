package com.example.milken.githubsearchapp.search

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.milken.githubsearchapp.MyApp
import com.example.milken.githubsearchapp.R
import com.example.milken.githubsearchapp.di.SearchModule
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.main_activity.*
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


    override fun initTextWatcher() {
        val textObservable = RxTextView.textChanges(searchEditText)
        presenter.setTextChangeObservable(textObservable)
    }

    override fun initSearchList() {

    }

    override fun updateSearchList() {

    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }
}
