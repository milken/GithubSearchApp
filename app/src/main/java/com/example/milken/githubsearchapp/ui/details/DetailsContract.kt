package com.example.milken.githubsearchapp.ui.details

import com.example.milken.githubsearchapp.data.common.RequestCallback
import com.example.milken.githubsearchapp.data.models.User

interface DetailsContract {

    interface Presenter : RequestCallback<User> {
        fun setView(view: View)
        fun trySetValidUserData(user: User?)

        fun viewSetUp()
        fun viewDestroyed()
    }

    interface View {
        fun finishWithError(message: String)
        fun continuteViewSetUp()

        fun configLoginText(login: String)
        fun configProfileImage(avatarUrl: String)
        fun configFollowersCountText(followersCount: Int)
        fun showError(message: String)
    }

    interface Repository {
        fun setRequestCallback(requestCallback: RequestCallback<User>)
        fun fetchUserDetails(user: String)
        fun viewDestroyed()
    }
}