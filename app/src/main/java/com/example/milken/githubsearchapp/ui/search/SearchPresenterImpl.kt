package com.example.milken.githubsearchapp.ui.search

import android.util.Log
import com.example.milken.githubsearchapp.data.models.BaseItem
import com.example.milken.githubsearchapp.data.models.User
import com.example.milken.githubsearchapp.utils.RxUtil
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import retrofit2.HttpException


class SearchPresenterImpl(

    private val searchRepository: SearchContract.Repository,
    private val rxUtil: RxUtil,
    private val compositeDisposable: CompositeDisposable
) : SearchContract.Presenter {
    private lateinit var view: SearchContract.View

    override fun setView(view: SearchContract.View) {
        this.view = view
    }

    override fun viewSetUp() {
        searchRepository.setRequestCallback(this)
        view.initSearchList()
        view.initTextWatcher()
    }

    override fun userClicked(baseItem: BaseItem) {
        view.startDetailsActivity(baseItem as User)
    }

    override fun setTextChangeObservable(textChangeObservable: Observable<CharSequence>) {
        val textChangeDisposable = rxUtil.searchObservableFrom(textChangeObservable)
            .subscribe(
                { processNewText(it) },
                { Log.e("myTag", "error in setTextChangeObservable - shouldn't happen") })

        compositeDisposable.add(textChangeDisposable)
    }

    private fun processNewText(text: String) {
        view.showProgressBar()
        searchRepository.fetchDataWith(text)
    }

    override fun viewDestroyed() {
        compositeDisposable.dispose()
    }

    override fun requestSuccess(requestResult: List<BaseItem>) {
        view.hideProgressBar()
        view.updateSearchList(requestResult)
    }

    override fun requestError(message: Throwable) {
        view.hideProgressBar()
        processError(message)
    }

    private fun processError(err: Throwable?) {
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

}