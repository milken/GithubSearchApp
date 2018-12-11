package com.example.milken.githubsearchapp.ui.search

import android.util.Log
import com.example.milken.githubsearchapp.data.apis.GithubSearchApi
import com.example.milken.githubsearchapp.data.models.BaseItem
import com.example.milken.githubsearchapp.data.models.ReposResponse
import com.example.milken.githubsearchapp.data.models.User
import com.example.milken.githubsearchapp.data.models.UsersResponse
import io.reactivex.Observable
import io.reactivex.Scheduler
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

    override fun userClicked(baseItem: BaseItem) {
        view.startDetailsActivity(baseItem as User)
    }

    override fun setTextChangeObservable(textChangeObservable: Observable<CharSequence>) {
        textChangeDisposable = textChangeObservable
            .debounce(250, TimeUnit.MILLISECONDS, Schedulers.io())
            .distinct()
            .filter { text -> !text.isBlank() }
            .subscribe({
                processTextChange(it.toString())
            }, {
                Log.e("myTag", "error in textChangeObservable")
            })
    }

    override fun viewDestroyed() {
        requestDisposable?.dispose()
        textChangeDisposable?.dispose()
    }

    private fun processTextChange(text: String) {
        view.showProgressBar()
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
                { result ->
                    view.updateSearchList(result)
                    view.hideProgressBar()
                },
                { err -> processError(err) })

    private fun processError(err: Throwable?) {
        view.hideProgressBar()

        err?.let {
            if (err is HttpException) {
                Log.d("myTag", "err response = ${err.response()}")

                if (err.code() == 403) {
                    view.showError("You have exceeded requests limit")
                    return
                }
            }
            Log.d("myTag", "err = ${err.localizedMessage}")
            view.showError(err.localizedMessage)
        }
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