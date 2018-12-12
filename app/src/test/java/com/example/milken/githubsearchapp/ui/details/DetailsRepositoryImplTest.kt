package com.example.milken.githubsearchapp.ui.details

import com.example.milken.githubsearchapp.data.apis.GithubUserDetailsApi
import com.example.milken.githubsearchapp.data.common.RequestCallback
import com.example.milken.githubsearchapp.data.models.User
import com.example.milken.githubsearchapp.data.models.UsersResponse
import com.example.milken.githubsearchapp.utils.ErrorParser
import com.example.milken.githubsearchapp.utils.SchedulerProviderFake
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.ResponseBody

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DetailsRepositoryImplTest {

    private val requestCallback = mockk<RequestCallback<User>>(relaxed = true)
    private val userDetailsApi = mockk<GithubUserDetailsApi>(relaxed = true)
    private val schedulerProvider = SchedulerProviderFake()
    private val errorParser = ErrorParser()

    private val currentDisposableMock = mockk<Disposable>(relaxed = true)

    private val detailsRepository = DetailsRepositoryImpl(userDetailsApi, schedulerProvider, errorParser).apply {
        this.setRequestCallback(requestCallback)
    }

    private val user = User(id = 1, login = "Adam", avatarUrl = "avatar_url", detailsUrl = "url", followersCount = 10)
    private val query = "login"

    @BeforeEach
    fun setUp() {
        clearMocks(userDetailsApi, requestCallback)
    }

    @Test
    fun fetchUserDetails_assertRequestSuccess_sendUserData() {
        every { userDetailsApi.getUserDetails(any()) } returns Observable.just(user)

        detailsRepository.fetchUserDetails(query)

        verify {
            requestCallback.requestSuccess(user)
        }
    }


    @Test
    fun fetchDataWith_assertRequestFailsWithException_callsRequestErrorWithCorrectMessage() {
        val message = "error"
        val exception = Exception(message)
        every { userDetailsApi.getUserDetails(any()) } returns Observable.error(exception)

        detailsRepository.fetchUserDetails(query)

        verify {
            requestCallback.requestError(message)
        }
    }

    @Test
    fun fetchDataWith_assertRequestFailsWithHttp403Exception_callsRequestErrorWithCorrectMessage() {
        val exception = HttpException(Response.error<Any>(403, ResponseBody.create(MediaType.parse("text/plain"), "Forbidden")))
        every { userDetailsApi.getUserDetails(any()) } returns Observable.error(exception)

        detailsRepository.fetchUserDetails(query)

        verify {
            requestCallback.requestError(ErrorParser.error403Message)
        }
    }

    @Test
    fun viewDestroyed() {
        detailsRepository.currentRequestDisposable = currentDisposableMock
        detailsRepository.viewDestroyed()

        verify {
            currentDisposableMock.dispose()
        }
    }
}