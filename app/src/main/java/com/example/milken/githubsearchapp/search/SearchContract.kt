package com.example.milken.githubsearchapp.search

interface SearchContract {

    interface Presenter {
        fun textChanged(text: String)

        fun start()
        fun stop()
    }

    interface View {
        fun showSearchList()
        fun showError()

        fun showProgressBar()
        fun hideProgressBar()
    }
}