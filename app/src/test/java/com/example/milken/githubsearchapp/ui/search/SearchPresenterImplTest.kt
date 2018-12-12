package com.example.milken.githubsearchapp.ui.search

import com.example.milken.githubsearchapp.data.models.BaseItem
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

    private val testScheduler = Schedulers.trampoline()
    private val rxUtil = RxUtil(testScheduler)

    private val searchRepository = mockk<SearchContract.Repository>(relaxed = true)
    private val compositeDisposable = mockk<CompositeDisposable>(relaxed = true)
    private val view = mockk<SearchContract.View>(relaxed = true)

    private val searchPresenter: SearchContract.Presenter =
        SearchPresenterImpl(searchRepository, rxUtil, compositeDisposable)

    private val textObservable = PublishSubject.create<CharSequence>()

    @Before
    fun setUp() {
        searchPresenter.setView(view)
        searchPresenter.setTextChangeObservable(textObservable)
    }

    @Test
    fun viewSetUp_assertInitSearchListWithView_initTextWatcherWithView_repositorySetCallback() {
        searchPresenter.viewSetUp()

        verifyAll {
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
    fun setTextChangeObservable_disposableIsAddedToCompositeDisposable() {
        searchPresenter.setTextChangeObservable(textObservable)

        verifyAll {
            compositeDisposable.add(any())
        }
    }

    @Test
    fun textObservableOnNext_showProgressBarWithView_searchRepositoryFetchDataWithCorrectText() {
        val text = "asd"
        textObservable.onNext(text)

        verifyAll {
            view.showProgressBar()
            searchRepository.fetchDataWith(text)
        }
    }

    @Test
    fun viewDestroyed_compositeDisposableDispose() {
        searchPresenter.viewDestroyed()

        verify {
            compositeDisposable.dispose()
        }
    }

    @Test
    fun requestSuccess_viewHideProgressBar_viewUpdateSearchList(){
        val list = listOf<BaseItem>()
        searchPresenter.requestSuccess(list)

        verifyAll {
            view.hideProgressBar()
            view.updateSearchList(list)
        }
    }

    @Test
    fun requestFailure_viewHideProgressBar_viewShowError(){
        val message = "error"
        searchPresenter.requestError(message)

        verifyAll {
            view.hideProgressBar()
            view.showError(message)
        }
    }
}