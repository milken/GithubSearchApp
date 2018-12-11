package com.example.milken.githubsearchapp.utils

import kotlin.reflect.jvm.internal.impl.name.Name.special
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers


internal class SchedulerProviderImpl : SchedulerProvider {

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()
    override fun computation(): Scheduler = Schedulers.computation()
    override fun io(): Scheduler = Schedulers.io()
}