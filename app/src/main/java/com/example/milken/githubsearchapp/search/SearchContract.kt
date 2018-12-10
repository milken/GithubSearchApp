package com.example.milken.githubsearchapp.search

interface SearchContract {

    interface Presenter {
        fun textChanged(text: String)
        fun setView(view: View)

        fun viewSetUp()
        fun viewDestroyed()
    }

    interface View {
        fun showSearchList()
        fun showError()

        fun showProgressBar()
        fun hideProgressBar()
    }
}