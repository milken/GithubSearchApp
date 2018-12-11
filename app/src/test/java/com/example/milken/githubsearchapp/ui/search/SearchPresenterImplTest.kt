package com.example.milken.githubsearchapp.ui.search

import com.example.milken.githubsearchapp.data.apis.GithubSearchApi
import com.example.milken.githubsearchapp.data.models.User
import com.example.milken.githubsearchapp.utils.SchedulerProviderFake
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class SearchPresenterImplTest {

    private lateinit var searchPresenterImpl: SearchPresenterImpl

    private val githubSearchApi = mockk<GithubSearchApi>(relaxed = true)
    private val view = mockk<SearchContract.View>(relaxed = true)
    private val testScheduler = SchedulerProviderFake()

    @Before
    fun setUp() {

        searchPresenterImpl = SearchPresenterImpl(githubSearchApi, testScheduler)
        searchPresenterImpl.setView(view)
    }

    @Test
    fun viewSetUp_assertInitSearchListAndInitTextWatcherWithView() {
        searchPresenterImpl.viewSetUp()

        verify {
            view.initSearchList()
            view.initTextWatcher()
        }
    }

    @Test
    fun userClicked_assertStartDetailsActivityWithView() {
        val user = mockk<User>()
        searchPresenterImpl.userClicked(user)

        verify {
            view.startDetailsActivity(user)
        }
    }

    @Test
    fun setTextChangeObservable() {

    }

    @Test
    fun viewDestroyed() {

    }
}