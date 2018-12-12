package com.example.milken.githubsearchapp.utils

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RxUtil(private val scheduler: Scheduler) {

     fun searchObservableFrom(textChangeObservable: Observable<CharSequence>): Observable<String> {
        return textChangeObservable
            .debounce(DEBOUNCE_TIME, TimeUnit.MILLISECONDS, scheduler)
            .skip(1) // after setting it to editText it emits current value
            .distinct()
            .filter { text -> !text.isBlank() }
            .map { it.toString() }
    }

    companion object {
        const val DEBOUNCE_TIME = 250L
    }
}