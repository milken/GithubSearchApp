package com.example.milken.githubsearchapp.search

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.milken.githubsearchapp.MyApp
import com.example.milken.githubsearchapp.R
import com.example.milken.githubsearchapp.di.SearchModule
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.main_activity.*
import java.util.concurrent.TimeUnit
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

    @SuppressLint("CheckResult")
    override fun initTextWatcher() {
        Observable.create(ObservableOnSubscribe<String> { subscriber ->
            searchEditText.addTextChangedListener(object : TextWatcherFacade {
                override fun afterTextChanged(s: Editable?) {
                    subscriber.onNext(s.toString())
                }
            })
        })
            .debounce(250, TimeUnit.MILLISECONDS)
            .distinct()
            .filter { text -> !text.isBlank() }
            .subscribe {
                presenter.textChanged(it)
            }
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
