package com.example.milken.githubsearchapp.ui.details

import com.example.milken.githubsearchapp.data.models.User
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DetailsPresenterImplTest {

    private val detailsRepository = mockk<DetailsContract.Repository>(relaxed = true)
    private val view = mockk<DetailsContract.View>(relaxed = true)

    private val detailsPresenter = DetailsPresenterImpl(detailsRepository).apply {
        this.setView(view)
    }

    private val nullUser = null
    private val basicUser = User(id = 1, login = "Adam", avatarUrl = "avatar_url", detailsUrl = "url")
    private val detailedUser = User(id = 1, login = "Edam", avatarUrl = "evatar_url", detailsUrl = "erl", followersCount = 10)

    @BeforeEach
    fun setUp() {
        clearMocks(detailsRepository, view)
        detailsPresenter.user = basicUser
    }

    @Test
    fun trySetValidUserData_withNull_callsViewFinishWithError() {
        detailsPresenter.trySetValidUserData(nullUser)

        verify {
            view.finishWithError(DetailsPresenterImpl.FINISH_ERROR_MESSAGE)
        }
        verify(inverse = true) {
            view.continuteViewSetUp()
        }
    }

    @Test
    fun trySetValidUserData_withUser_callsViewContinueSetUp() {
        detailsPresenter.trySetValidUserData(basicUser)

        verify {
            view.continuteViewSetUp()
        }
        verify(inverse = true) {
            view.finishWithError(any())
        }
    }

    @Test
    fun viewSetUp_withBasicUser_callsAll() {
        detailsPresenter.viewSetUp()

        verifyAll {
            detailsRepository.setRequestCallback(detailsPresenter)
            view.configLoginText(basicUser.login)
            view.configProfileImage(basicUser.avatarUrl)
            detailsRepository.fetchUserDetails(basicUser.login)
        }

        verify(inverse = true) {
            view.configFollowersCountText(any())
        }
    }

    @Test
    fun requestSuccess() {
        detailsPresenter.requestSuccess(detailedUser)

        verifyAll {
            view.configLoginText(detailedUser.login)
            view.configProfileImage(detailedUser.avatarUrl)
            view.configFollowersCountText(detailedUser.followersCount!!)
        }
    }

    @Test
    fun requestError() {
        val error = "error"
        detailsPresenter.requestError(error)

        verify {
            view.showError(error)
        }
    }

    @Test
    fun viewDestroyed() {
        detailsPresenter.viewDestroyed()

        verify {
            detailsRepository.viewDestroyed()
        }
    }
}