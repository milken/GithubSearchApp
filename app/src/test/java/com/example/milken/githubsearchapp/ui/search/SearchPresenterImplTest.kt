package com.example.milken.githubsearchapp.ui.search

import com.example.milken.githubsearchapp.data.apis.GithubSearchApi
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

    @Before
    fun setUp() {

        searchPresenterImpl = SearchPresenterImpl(githubSearchApi)
        searchPresenterImpl.setView(view)
    }

    @Test
    fun viewSetUpAssertViewInitSearchListAndTextWatcher() {
        searchPresenterImpl.viewSetUp()

        verify {
            view.initSearchList()
            view.initTextWatcher()
        }
    }

    @Test
    fun userClicked() {

    }

    @Test
    fun setTextChangeObservable() {

    }

    @Test
    fun viewDestroyed() {

    }
}