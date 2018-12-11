package com.example.milken.githubsearchapp.ui.search

import com.example.milken.githubsearchapp.data.models.User
import com.example.milken.githubsearchapp.utils.RxUtil
import com.example.milken.githubsearchapp.utils.SchedulerProviderFake
import io.mockk.impl.annotations.MockK
import io.mockk.impl.stub.MockKStub
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test

class SearchPresenterImplTest {

    private val testScheduler = SchedulerProviderFake()
    private lateinit var rxUtil: RxUtil


    private lateinit var searchPresenter: SearchContract.Presenter

    private val searchRepository = mockk<SearchContract.Repository>(relaxed = true)
    private val compositeDisposable = mockk<CompositeDisposable>()

    private val view = mockk<SearchContract.View>(relaxed = true)

//    private val textObservable = PublishSubject.create<CharSequence>()

    @Before
    fun setUp() {
        rxUtil = RxUtil(testScheduler.io())

        searchPresenter = SearchPresenterImpl(searchRepository, rxUtil, compositeDisposable)
        searchPresenter.setView(view)
    }

    @Test
    fun viewSetUp_assertInitSearchListAndInitTextWatcherWithView() {
        searchPresenter.viewSetUp()

        verify {
            searchRepository.setRequestCallback(searchPresenter)
            view.initSearchList()
            view.initTextWatcher()
        }
    }

    @Test
    fun userClicked_assertStartDetailsActivityWithView() {
        val user = mockk<User>()
        searchPresenter.userClicked(user)

        verify {
            view.startDetailsActivity(user)
        }
    }

    @Test
    fun setTextChangeObservable() {
//        searchPresenter.setTextChangeObservable(textObservable)
//
//        val sth = rxUtil.searchObservableFrom(textObservable).subscribe()
////        textObservable.onNext("abc")
//
//        verify {
//            compositeDisposable.add(sth)
//        }
////        searchPresenter.setTextChangeObservable(subject)
    }

    @Test
    fun viewDestroyed() {
        searchPresenter.viewDestroyed()

        verify {
            compositeDisposable.dispose()
        }
    }
}