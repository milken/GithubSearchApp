package com.example.milken.githubsearchapp.ui.search

import com.example.milken.githubsearchapp.data.apis.GithubSearchApi
import com.example.milken.githubsearchapp.data.common.RequestCallback
import com.example.milken.githubsearchapp.data.models.*
import com.example.milken.githubsearchapp.utils.SchedulerProviderFake
import io.mockk.mockk
import io.mockk.every
import io.mockk.verify
import io.mockk.verifyOrder
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.junit.Test

import org.junit.Before
import java.lang.Exception

class SearchRepositoryImplTest {


    private val requestCallback = mockk<RequestCallback<List<BaseItem>>>(relaxed = true)
    private val githubSearchApi = mockk<GithubSearchApi>(relaxed = true)
    private val compositeDisposable = mockk<CompositeDisposable>(relaxed = true)
    private val schedulerProvider = SchedulerProviderFake()

    private val searchRepository = SearchRepositoryImpl(githubSearchApi, schedulerProvider, compositeDisposable)

    private val userId1 = User(id = 1, login = "Adam", avatarUrl = "avatar_url", detailsUrl = "url")
    private val userId10 = User(id = 10, login = "Alicja", avatarUrl = "avatar_url", detailsUrl = "url")

    private val repoId2 = Repo(id = 2, name = "RxKotlin", description = "Rx version of kotlin")
    private val repoId3 = Repo(id = 3, name = "Testable", description = null)

    private val requestQuery = "asd"

    private val currentDisposableMock = mockk<Disposable>(relaxed = true)

    @Before
    fun setUp(){
        searchRepository.setRequestCallback(requestCallback)
    }

    @Test
    fun fetchDataWith_assertDisposeCurrentRequest() {
        searchRepository.currentRequestDisposable = currentDisposableMock
        searchRepository.fetchDataWith(requestQuery)

        verify {
            currentDisposableMock.dispose()
        }
    }

    @Test
    fun fetchDataWith_assertCompositeDisposableClearAndAdd() {
        searchRepository.fetchDataWith(requestQuery)

        verifyOrder {
            compositeDisposable.clear()
            compositeDisposable.add(any())
        }
    }

    @Test
    fun fetchDataWith_assertRequestSuccessWithSortedList() {
        val userList = listOf(userId1, userId10)
        val repoList = listOf(repoId2, repoId3)

        val resultList = listOf<BaseItem>(userId1, repoId2, repoId3, userId10)

        val userResponse = UsersResponse(userList)
        val reposResponse = ReposResponse(repoList)

        every { githubSearchApi.getUserList("asd") } returns Observable.just(userResponse)
        every { githubSearchApi.getRepoList("asd") } returns Observable.just(reposResponse)

        searchRepository.fetchDataWith("asd")

        verify{
            requestCallback.requestSuccess(resultList)
        }
    }

    @Test
    fun fetchDataWith_assertRequestFailureWithMessage() {
        val userList = listOf(userId1, userId10)
        val error = Exception("error")
        val userResponse = UsersResponse(userList)

        every { githubSearchApi.getUserList("asd") } returns Observable.just(userResponse)
        every { githubSearchApi.getRepoList("asd") } returns (Observable.error(error))

        searchRepository.fetchDataWith("asd")

        verify{
            requestCallback.requestError(error)
        }
    }


    @Test
    fun viewDestroyed() {
        searchRepository.viewDestroyed()

        verify {
            compositeDisposable.dispose()
        }
    }
}