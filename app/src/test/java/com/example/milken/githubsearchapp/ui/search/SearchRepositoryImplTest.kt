package com.example.milken.githubsearchapp.ui.search

import com.example.milken.githubsearchapp.data.apis.GithubSearchApi
import com.example.milken.githubsearchapp.data.common.RequestCallback
import com.example.milken.githubsearchapp.data.models.*
import com.example.milken.githubsearchapp.utils.ErrorParser
import com.example.milken.githubsearchapp.utils.SchedulerProviderFake
import io.mockk.mockk
import io.mockk.every
import io.mockk.verify
import io.mockk.verifyOrder
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Test

import org.junit.Before
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception

class SearchRepositoryImplTest {


    private val requestCallback = mockk<RequestCallback<List<BaseItem>>>(relaxed = true)
    private val githubSearchApi = mockk<GithubSearchApi>(relaxed = true)
    private val schedulerProvider = SchedulerProviderFake()
    private val errorParser = ErrorParser()

    private val searchRepository = SearchRepositoryImpl(githubSearchApi, schedulerProvider, errorParser)

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
    fun fetchDataWith_assertRequestSuccess_sendsSortedList() {
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
    fun fetchDataWith_assertRequestFailsWithException_callsRequestErrorWithCorrectMessage() {
        val userList = listOf(userId1, userId10)
        val message = "error"
        val exception = Exception(message)

        val userResponse = UsersResponse(userList)

        every { githubSearchApi.getUserList("asd") } returns Observable.just(userResponse)
        every { githubSearchApi.getRepoList("asd") } returns (Observable.error(exception))

        searchRepository.fetchDataWith("asd")

        verify{
            requestCallback.requestError(message)
        }
    }

    @Test
    fun fetchDataWith_assertRequestFailsWithHttp403Exception_callsRequestErrorWithCorrectMessage() {
        val userList = listOf(userId1, userId10)
        val exception = HttpException(Response.error<Any>(403, ResponseBody.create(MediaType.parse("text/plain"), "Forbidden")))

        val userResponse = UsersResponse(userList)

        every { githubSearchApi.getUserList("asd") } returns Observable.just(userResponse)
        every { githubSearchApi.getRepoList("asd") } returns (Observable.error(exception))

        searchRepository.fetchDataWith("asd")

        verify{
            requestCallback.requestError(ErrorParser.error403Message)
        }
    }

    @Test
    fun viewDestroyed_assertCurrentRequestDisposed() {
        searchRepository.currentRequestDisposable = currentDisposableMock
        searchRepository.viewDestroyed()

        verify {
            currentDisposableMock.dispose()
        }
    }
}