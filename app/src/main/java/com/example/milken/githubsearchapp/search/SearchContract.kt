package com.example.milken.githubsearchapp.search

interface SearchContract {

    interface Presenter {
        fun textChanged(text: String)
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