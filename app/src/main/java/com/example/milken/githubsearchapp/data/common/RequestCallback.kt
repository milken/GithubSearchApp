package com.example.milken.githubsearchapp.data.common

interface RequestCallback<T> {

    fun requestSuccess(requestResult: T)
    fun requestError(message: Throwable)
}