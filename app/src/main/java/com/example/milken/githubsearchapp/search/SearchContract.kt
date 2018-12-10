package com.example.milken.githubsearchapp.search

import io.reactivex.Observable

interface SearchContract {

    interface Presenter {
        fun setTextChangeObservable(textObservable: Observable<CharSequence>)
        fun setView(view: View)

        fun viewSetUp()
        fun viewDestroyed()
    }

    interface View {
        fun initSearchList()
        fun initTextWatcher()

        fun updateSearchList()
        fun showError(message: String)

        fun showProgressBar()
        fun hideProgressBar()
    }
}