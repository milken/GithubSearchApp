package com.example.milken.githubsearchapp.ui.search

import com.example.milken.githubsearchapp.data.models.BaseItem
import com.example.milken.githubsearchapp.data.models.User
import com.example.milken.githubsearchapp.utils.RxUtil
import io.mockk.clearMocks
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchPresenterImplTest {

    private val testScheduler = Schedulers.trampoline()
    private val rxUtil = RxUtil(testScheduler)

    private val searchRepository = mockk<SearchContract.Repository>(relaxed = true)
    private val compositeDisposable = mockk<CompositeDisposable>(relaxed = true)
    private val view = mockk<SearchContract.View>(relaxed = true)

    private val textObservable = PublishSubject.create<CharSequence>()

    private val searchPresenter =
        SearchPresenterImpl(searchRepository, rxUtil, compositeDisposable).apply {
            this.setView(view)
            this.setTextChangeObservable(textObservable)
            textObservable.onNext("") //mock first emit after setting up textWatcher
        }


    @BeforeEach
    fun setUp() {
        clearMocks(searchRepository, compositeDisposable, view)
    }

    @Test
    fun viewSetUp_assertInitSearchListWithView_initTextWatcherWithView_repositorySetCallback() {
        searchPresenter.viewSetUp()

        verifyAll {
            viewSetUp_calls()
        }
    }

    @Test
    fun viewSetUp_withRestoredData_additionalyCallsUpdateSearchListWithView(){
        val data = listOf(User(id = 123, detailsUrl = "details_url", avatarUrl = "avatar_url", login = "Adam"))
        searchPresenter.data = data

        searchPresenter.viewSetUp()

        verifyAll {
            viewSetUp_calls()
            view.updateSearchList(data)
        }
    }

    private fun viewSetUp_calls() {

            searchRepository.setRequestCallback(searchPresenter)
            view.initSearchList()
            view.initTextWatcher()
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
    fun viewDestroyed_compositeDisposableDispose_viewDestroyedWithSearchRepository() {
        searchPresenter.viewDestroyed()

        verifyAll {
            compositeDisposable.dispose()
            searchRepository.viewDestroyed()
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

    @Test
    fun getDataParcel_createsObjectWithCorrectList() {
        assert(searchPresenter.getDataParcel().itemList == searchPresenter.data)
    }
}