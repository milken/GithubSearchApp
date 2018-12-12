package com.example.milken.githubsearchapp.ui.details

import android.support.annotation.VisibleForTesting
import com.example.milken.githubsearchapp.data.models.User

class DetailsPresenterImpl(
    private val detailsRepository: DetailsContract.Repository
) :
    DetailsContract.Presenter {

    private lateinit var view: DetailsContract.View
    @VisibleForTesting
    lateinit var user: User

    override fun setView(view: DetailsContract.View) {
        this.view = view
    }

    override fun trySetValidUserData(user: User?) {
        if (user == null) {
            view.finishWithError(FINISH_ERROR_MESSAGE)
            return
        }

        this.user = user
        this.view.continuteViewSetUp()
    }

    override fun viewSetUp() {
        detailsRepository.setRequestCallback(this)

        updateUI()

        if (!user.hasDetails) {
            detailsRepository.fetchUserDetails(user.login)
        }
    }

    private fun updateUI() {
        view.configLoginText(user.login)
        view.configProfileImage(user.avatarUrl)

        user.followersCount?.let {
            view.configFollowersCountText(it)
        }
    }

    override fun requestSuccess(requestResult: User) {
        this.user = requestResult
        updateUI()
    }

    override fun requestError(message: String) {
        view.showError(message)
    }

    override fun viewDestroyed() {
        detailsRepository.viewDestroyed()
    }

    companion object {
        const val FINISH_ERROR_MESSAGE = "No user data!"
    }
}