package com.example.milken.githubsearchapp.search

import android.util.Log
import com.example.milken.githubsearchapp.data.apis.GithubSearchApi
import com.example.milken.githubsearchapp.data.models.BaseItem
import com.example.milken.githubsearchapp.data.models.ReposResponse
import com.example.milken.githubsearchapp.data.models.UsersResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.util.concurrent.TimeUnit


class SearchPresenterImpl(
    val githubSearchApi: GithubSearchApi
) : SearchContract.Presenter {

    private lateinit var view: SearchContract.View

    private var textChangeDisposable: Disposable? = null
    private var requestDisposable: Disposable? = null

    override fun setView(view: SearchContract.View) {
        this.view = view
    }

    override fun viewSetUp() {
        view.initSearchList()
        view.initTextWatcher()
    }


    override fun setTextChangeObservable(textObservable: Observable<CharSequence>) {
        textChangeDisposable = textObservable
            .debounce(250, TimeUnit.MILLISECONDS)
            .distinct()
            .filter { text -> !text.isBlank() }
            .subscribe {
                processTextChange(it.toString())
            }
    }

    override fun viewDestroyed() {
        requestDisposable?.dispose()
        textChangeDisposable?.dispose()
    }

    private fun processTextChange(text: String) {
        requestDisposable?.dispose()

        requestDisposable = createRequestObservable(text)
    }

    private fun createRequestObservable(query: String): Disposable =
        Observable
            .zip(
                getUserListRequest(query),
                getRepoListRequest(query),
                BiFunction<UsersResponse, ReposResponse, List<BaseItem>> { (userList), (repoList) ->
                    val resultList = arrayListOf<BaseItem>()
                    resultList.addAll(userList)
                    resultList.addAll(repoList)
                    resultList
                })
            .observeOn(Schedulers.computation())
            .flatMapIterable { t1 -> t1 }
            .sorted { t1, t2 -> (t1.id - t2.id).toInt() }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> Log.d("myTag", "result size = ${result.size}") },
                { err -> view.showError(err.localizedMessage) })


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