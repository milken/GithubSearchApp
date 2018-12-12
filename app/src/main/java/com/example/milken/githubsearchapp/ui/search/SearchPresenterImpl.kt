package com.example.milken.githubsearchapp.ui.search

import android.util.Log
import com.example.milken.githubsearchapp.data.models.BaseItem
import com.example.milken.githubsearchapp.data.models.SearchDataParcel
import com.example.milken.githubsearchapp.data.models.User
import com.example.milken.githubsearchapp.utils.RxUtil
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable


class SearchPresenterImpl(

    private val searchRepository: SearchContract.Repository,
    private val rxUtil: RxUtil,
    private val compositeDisposable: CompositeDisposable
) : SearchContract.Presenter {

    private lateinit var view: SearchContract.View
    private var data: List<BaseItem> = emptyList()

    override fun setView(view: SearchContract.View) {
        this.view = view
    }

    override fun dataRestored(searchDataParcel: SearchDataParcel) {
        data = searchDataParcel.itemList
    }

    override fun viewSetUp() {
        searchRepository.setRequestCallback(this)
        view.initSearchList()
        view.initTextWatcher()

        if(data.isNotEmpty()) {
            view.updateSearchList(data)
        }
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
        searchRepository.viewDestroyed()
        compositeDisposable.dispose()
    }

    override fun requestSuccess(requestResult: List<BaseItem>) {
        data = requestResult
        view.hideProgressBar()
        view.updateSearchList(data)
    }

    override fun requestError(message: String) {
        view.hideProgressBar()
        view.showError(message)
    }

    override fun getDataParcel(): SearchDataParcel {
        return SearchDataParcel(data)
    }



}