package com.example.milken.githubsearchapp.search

import com.example.milken.githubsearchapp.data.models.BaseItem
import com.example.milken.githubsearchapp.data.models.User
import io.reactivex.Observable

interface SearchContract {

    interface Presenter {
        fun setTextChangeObservable(textObservable: Observable<CharSequence>)
        fun setView(view: View)

        fun userClicked(baseItem: BaseItem)

        fun viewSetUp()
        fun viewDestroyed()
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
}