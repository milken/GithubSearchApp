package com.example.milken.githubsearchapp.utils

import android.util.Log
import retrofit2.HttpException

class ErrorParser {
    companion object {
        const val error403Message = "You have exceeded requests limit"
    }

    fun getMessage(throwable: Throwable): String {
        if(is403Error(throwable)){
            return error403Message
        }
        return throwable.localizedMessage
    }

    private fun is403Error(throwable: Throwable) =
        throwable is HttpException &&
        throwable.code() == 403
}