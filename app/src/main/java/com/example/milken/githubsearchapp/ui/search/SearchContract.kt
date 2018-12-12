package com.example.milken.githubsearchapp.ui.search

import com.example.milken.githubsearchapp.data.common.RequestCallback
import com.example.milken.githubsearchapp.data.models.BaseItem
import com.example.milken.githubsearchapp.data.models.SearchDataParcel
import com.example.milken.githubsearchapp.data.models.User
import io.reactivex.Observable

interface SearchContract {

    interface Presenter : RequestCallback<List<BaseItem>> {
        fun setTextChangeObservable(textChangeObservable: Observable<CharSequence>)
        fun setView(view: View)

        fun userClicked(baseItem: BaseItem)

        fun viewSetUp()
        fun viewDestroyed()

        fun getDataParcel(): SearchDataParcel
        fun dataRestored(searchDataParcel: SearchDataParcel)
    }

    interface View {
        fun initSearchList()
        fun initTextWatcher()

        fun updateSearchList(items: List<BaseItem>)
        fun showError(message: String)

        fun showProgressBar()
        fun hideProgressBar()
        fun startDetailsActivity(user: User)
    }

    interface Repository {
        fun setRequestCallback(requestCallback: RequestCallback<List<BaseItem>>)
        fun fetchDataWith(query: String)
        fun viewDestroyed()
    }
}