package com.example.milken.githubsearchapp.search

import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.example.milken.githubsearchapp.MyApp
import com.example.milken.githubsearchapp.R
import com.example.milken.githubsearchapp.data.models.BaseItem
import com.example.milken.githubsearchapp.di.SearchModule
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity(), SearchContract.View {

    @Inject
    lateinit var presenter: SearchContract.Presenter

    private lateinit var adapter: SearchAdapter

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
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SearchAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            MarginItemDecorator(
                resources.getDimension(R.dimen.item_default_margin).toInt()
            )
        )
    }

    override fun updateSearchList(items: List<BaseItem>) {
        adapter.setData(items)
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
