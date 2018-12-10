package com.example.milken.githubsearchapp.search

import android.text.Editable
import android.util.Log
import com.example.milken.githubsearchapp.data.apis.GithubSearchApi
import com.example.milken.githubsearchapp.data.models.ReposResponse
import com.example.milken.githubsearchapp.data.models.UsersResponse
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class SearchPresenterImpl(
    val githubSearchApi: GithubSearchApi
) : SearchContract.Presenter {

    private lateinit var view: SearchContract.View

    private val compositeDisposable = CompositeDisposable()

    override fun setView(view: SearchContract.View) {
        this.view = view
    }

    override fun viewSetUp() {
        view.initSearchList()
        view.initTextWatcher()
    }

    override fun setTextChangeObservable(textObservable: Observable<CharSequence>) {
        val disposable = textObservable
            .debounce(250, TimeUnit.MILLISECONDS)
            .distinct()
            .filter { text -> !text.isBlank() }
            .subscribe {
                Log.d("myTag", "text = $it")
            }

        compositeDisposable.add(disposable)
    }

    override fun viewDestroyed() {
        compositeDisposable.dispose()
    }

    private fun getUserListRequest(query: String): Observable<UsersResponse> =
        githubSearchApi
            .getUserList(query)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())

    private fun getRepoListRequest(query: String): Observable<ReposResponse> =
        githubSearchApi
            .getRepoList(query)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
}