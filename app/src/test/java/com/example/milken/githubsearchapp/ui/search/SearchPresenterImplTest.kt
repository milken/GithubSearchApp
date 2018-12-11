package com.example.milken.githubsearchapp.ui.search

import com.example.milken.githubsearchapp.data.models.User
import com.example.milken.githubsearchapp.utils.RxUtil
import com.example.milken.githubsearchapp.utils.SchedulerProviderFake
import io.mockk.impl.annotations.MockK
import io.mockk.impl.stub.MockKStub
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
import net.bytebuddy.matcher.ElementMatchers.any
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class SearchPresenterImplTest {

    private val testScheduler = TestScheduler()
    private val rxUtil = RxUtil(testScheduler)


    private val searchRepository = mockk<SearchContract.Repository>(relaxed = true)
    private val compositeDisposable = CompositeDisposable()
    private val view = mockk<SearchContract.View>(relaxed = true)

    private val searchPresenter = SearchPresenterImpl(searchRepository, rxUtil, compositeDisposable)

    private val textObservable = PublishSubject.create<CharSequence>()

    @Before
    fun setUp() {
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
        searchPresenter.setTextChangeObservable(textObservable)

        val sth = rxUtil.searchObservableFrom(textObservable).subscribe()
        textObservable.onNext("abc")
        testScheduler.advanceTimeBy(RxUtil.DEBOUNCE_TIME, TimeUnit.MILLISECONDS)

        verifyAll {
            compositeDisposable.add(sth)
            searchRepository.fetchDataWith("abc")
        }
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