package com.example.milken.githubsearchapp.ui.search

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.milken.githubsearchapp.ui.MyApp
import com.example.milken.githubsearchapp.R
import com.example.milken.githubsearchapp.data.models.BaseItem
import com.example.milken.githubsearchapp.data.models.SearchDataParcel
import com.example.milken.githubsearchapp.data.models.User
import com.example.milken.githubsearchapp.di.SearchModule
import com.example.milken.githubsearchapp.ui.details.DetailsActivity
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

        savedInstanceState?.let {
            it.getParcelable<SearchDataParcel>(DATA_KEY)?.let {
                it -> presenter.dataRestored(it)
            }
        }

        presenter.setView(this)
        presenter.viewSetUp()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(DATA_KEY, presenter.getDataParcel())
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
        adapter = SearchAdapter(this) {
            presenter.userClicked(it)
        }
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

    override fun startDetailsActivity(user: User) {
        startActivity(DetailsActivity.newIntent(this, user))
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showProgressBar() {
        runOnUiThread {
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun hideProgressBar() {
        runOnUiThread {
            progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val DATA_KEY = "SEARCH_LIST"
    }
}
