package com.example.milken.githubsearchapp.ui.search

sealed class ViewState<T> {
    data class Progress<T>(var loading: Boolean) : ViewState<T>()
    data class Success<T>(var data: T) : ViewState<T>()
    data class Failure<T>(val e: String) : ViewState<T>()

    companion object {
        fun <T> loading(isLoading: Boolean): ViewState<T> = Progress(isLoading)

        fun <T> success(data: T): ViewState<T> = Success(data)

        fun <T> failure(e: String): ViewState<T> = Failure(e)
    }}