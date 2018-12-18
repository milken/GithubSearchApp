package com.example.milken.githubsearchapp.ui.search

import com.example.milken.githubsearchapp.data.common.RequestCallback
import com.example.milken.githubsearchapp.data.models.BaseItem
import com.example.milken.githubsearchapp.data.models.SearchDataParcel
import com.example.milken.githubsearchapp.data.models.User
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface SearchContract {

    interface Presenter {
        fun setTextChangeObservable(textChangeObservable: Observable<CharSequence>)
        fun setView(view: View)

        val resultList: Observable<ViewState<List<BaseItem>>>

        fun userClicked(baseItem: BaseItem)

        fun viewSetUp()
        fun viewDestroyed()

        fun getDataParcel(): SearchDataParcel?
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

        val responseSubject: PublishSubject<ViewState<List<BaseItem>>>

        fun fetchDataWith(query: String)
        fun viewDestroyed()
    }
}